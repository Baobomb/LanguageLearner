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
import tw.bao.languagelearner.R
import tw.bao.languagelearner.info.contract.InfoContract
import tw.bao.languagelearner.info.contract.InfoPresenter
import tw.bao.languagelearner.info.utils.UtilsInfo
import tw.bao.languagelearner.model.WordData
import tw.bao.languagelearner.utils.db.UtilsDB

/**
 * Created by bao on 2017/12/9.
 */

class InfoFragment : Fragment(), InfoContract.View {

    private var mPresenter: InfoPresenter = InfoPresenter(this)

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
        mCircleAnswerCorrectRate.mCircleOuterStrokePercent = UtilsInfo.getUserAnswerCorrectRate().toInt()
        mTvAnswerCorrectRate.text = "${UtilsInfo.getUserAnswerCorrectRate()}%"
        mTvAnswerTotalNums.text = UtilsInfo.getUserAnswerTotalNums().toString()
        mTvAnswerCorrectNums.text = UtilsInfo.getUserAnswerCorrectNums().toString()
        mTvLevel.text = (UtilsInfo.getUserLevel() + 1).toString()
    }

    var nextWords: WordData? = null
    var mIsAnimNeedContinue = false
    var animator: ValueAnimator = ValueAnimator.ofFloat(0f, 3f).apply {
        addUpdateListener {
            val animatedValue = animatedValue as Float
            mClWordsPreview.alpha = when {
                animatedValue > 2f -> 3f - animatedValue
                animatedValue > 1f -> 1f
                else -> animatedValue
            }
        }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                nextWords?.apply {
                    mTvChineseWordsPreview.text = chineseWord
                    mTvEngWordsPreview.text = engWord
                    mTvRomanWordsPreview.text = romanText
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

}