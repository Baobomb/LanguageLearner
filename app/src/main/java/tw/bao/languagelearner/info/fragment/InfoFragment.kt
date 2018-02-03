package tw.bao.languagelearner.info.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_info_layout.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import tw.bao.adsdk.Definition
import tw.bao.adsdk.adobject.BaseAdObject
import tw.bao.adsdk.cache.AdCacheManager
import tw.bao.adsdk.listener.AdRequestStatusListener
import tw.bao.languagelearner.R
import tw.bao.languagelearner.info.contract.InfoContract
import tw.bao.languagelearner.info.contract.InfoPresenter
import tw.bao.languagelearner.info.utils.UtilsInfo
import tw.bao.languagelearner.model.WordData
import tw.bao.languagelearner.utils.Utils
import tw.bao.languagelearner.utils.db.UtilsDB

/**
 * Created by bao on 2017/12/9.
 */

class InfoFragment : Fragment(), InfoContract.View {

    private var mPresenter: InfoPresenter = InfoPresenter(this)
    private var mAdObject: BaseAdObject? = null

    companion object {
        private val LOG_TAG = InfoFragment::class.java.simpleName
        public val sInstance: InfoFragment by lazy { InfoFragment() }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mPresenter.setUserVisibleHint(isVisibleToUser)
        Log.d(LOG_TAG, "InfoFragment visible")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.onCreate()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mPresenter.onCreateView()
        return inflater?.inflate(R.layout.fragment_info_layout, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        mPresenter.onViewCreated()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        mPresenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
        loadAd()
    }

    override fun onPause() {
        super.onPause()
        mPresenter.onPause()
    }

    override fun onStop() {
        super.onStop()
        mPresenter.onStop()
    }

    override fun initView() {
        mCircleLevelExp.mCircleOuterStrokePercent = UtilsInfo.getUserCurrentScore()
        mTvExp.text = UtilsInfo.getUserCurrentScore().toString()
        mCircleAnswerCorrectRate.mCircleOuterStrokePercent = UtilsInfo.getUserAnswerCorrectRate().toInt()
        mTvAnswerCorrectRate.text = "${UtilsInfo.getUserAnswerCorrectRate()}%"
        mTvAnswerTotalNums.text = "共作答 : ${UtilsInfo.getUserAnswerTotalNums()}題"
        mTvAnswerCorrectNums.text = "共答對 : ${UtilsInfo.getUserAnswerCorrectNums()}題"
        mTvLevel.text = "等級 : ${(UtilsInfo.getUserLevel() + 1)}"
    }

    var nextWords: WordData? = null
    var mIsAnimNeedContinue = false
    var animator: ValueAnimator = ValueAnimator.ofFloat(0f, 3f).apply {
        addUpdateListener {
            val animatedValue = animatedValue as Float
            val alphaValue = when {
                animatedValue > 2f -> 3f - animatedValue
                animatedValue > 1f -> 1f
                else -> animatedValue
            }
            mLlEngWordsPreview?.alpha = alphaValue
            mLlChineseWordsPreview?.alpha = alphaValue
        }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                nextWords?.apply {
                    mTvChineseWordsPreview?.text = chineseWord
                    mTvEngWordsPreview?.text = engWord
                    mTvRomanWordsPreview?.text = romanText
                }
            }

            override fun onAnimationEnd(animation: Animator?) {
                prepareWords()
            }
        })
        duration = 5000
    }

    override fun startWordsPreviewAnim() {
        Log.d(LOG_TAG, "startWordsPreviewAnim")
        mIsAnimNeedContinue = true
        animWords()
    }

    override fun stopWordsPreviewAnim() {
        Log.d(LOG_TAG, "stopWordsPreviewAnim")
        mIsAnimNeedContinue = false
        nextWords = null
        animator.cancel()
    }

    private fun animWords() {
        if (!mIsAnimNeedContinue) {
            Log.d(LOG_TAG, "start anim words fail currentWords : mIsAnimNeedContinue false")
            return
        }
        if (nextWords == null) {
            Log.d(LOG_TAG, "start anim words fail nextWords : $nextWords")
            prepareWords()
            return
        }
        animator.start()
    }

    private fun prepareWords() {
        doAsync {
            nextWords = UtilsDB.getRandomWords(context)
            Log.d(LOG_TAG, "prepareWords nextWords : $nextWords")
            uiThread { animWords() }
        }
    }

    override fun getViewContext(): Context? = context

    private fun loadAd() {
        if (mCvAdContainer.childCount > 0) {
            return
        }
        if (mAdObject != null) {
            tryToRenderAd()
            tryToExpandAd(false)
            return
        }
        tw.bao.adsdk.AdManager.getInstance(Definition.AdUnit.INFO)
                .setIsUsingDebugAdUnit(true)
                .setAdSourceNeedRequest(Definition.AdSource.NATIVE, true)
                .setAdSourceNeedRequest(Definition.AdSource.BANNER, true)
                .setRequestStatusListener(object : AdRequestStatusListener {
                    override fun onRequestStart(adUnit: Definition.AdUnit) {
                        Log.d(LOG_TAG, "Start request ad")
                    }

                    override fun onRequestEnd(adUnit: Definition.AdUnit) {
                        Log.d(LOG_TAG, "Request end")
                        mAdObject = AdCacheManager.getCacheAd(Definition.AdUnit.INFO)
                        if (mAdObject == null) {
                            return
                        }
                        if (mCvAdContainer == null) {
                            return
                        }
                        mAdObject?.renderAd(context, mCvAdContainer)
                        tryToExpandAd(true)
                    }
                })
                .startRequest(context)
    }

    private fun tryToRenderAd() {
        mAdObject?.renderAd(context, mCvAdContainer)
    }

    private fun tryToExpandAd(isNeedSmoothExpand: Boolean) {
        if (mCvAdContainer == null) {
            return
        }
        if (!isNeedSmoothExpand) {
            mCvAdContainer.visibility = View.VISIBLE
            return
        }
        val valueAnimator = ValueAnimator.ofInt(0, Utils.dp2px(context, 70f))
        valueAnimator.addUpdateListener { valueAnimator ->
            if (mCvAdContainer != null) {
                mCvAdContainer.layoutParams.height = valueAnimator.animatedValue as Int
                mCvAdContainer.requestLayout()
            }
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                if (mCvAdContainer != null) {
                    mCvAdContainer.visibility = View.VISIBLE
                }
            }
        })
        valueAnimator.duration = 500
        valueAnimator.start()
    }

}