package tw.bao.languagelearner.info.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_info_layout.*
import tw.bao.languagelearner.R
import tw.bao.languagelearner.info.contract.InfoContract
import tw.bao.languagelearner.info.contract.InfoPresenter
import tw.bao.languagelearner.info.utils.UtilsInfo

/**
 * Created by bao on 2017/12/9.
 */

class InfoFragment : Fragment, InfoContract.View {
    constructor()

    private var mPresenter: InfoPresenter = InfoPresenter(this)

    companion object {
        public val sInstance: InfoFragment by lazy { InfoFragment() }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d("FragmentVisible", "InfoFragment visible")
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
        mCircleInfoView.mUserExpPercent = UtilsInfo.getUserCurrentScore()
        mTvAnswerCorrectRate.text = "${UtilsInfo.getUserAnswerCorrectRate()}%"
        mTvAnswerTotalNums.text = UtilsInfo.getUserAnswerTotalNums().toString()
        mTvAnswerCorrectNums.text = UtilsInfo.getUserAnswerCorrectNums().toString()
    }

    override fun startWordsPreviewAnim() {
    }

    override fun getViewContext(): Context? = context

}