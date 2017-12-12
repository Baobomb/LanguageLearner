package tw.bao.languagelearner.main.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tw.bao.languagelearner.R
import tw.bao.languagelearner.main.contract.MainActivityContract
import tw.bao.languagelearner.main.contract.MainActivityPresenter

/**
 * Created by bao on 2017/12/9.
 */

class MainFragment : Fragment, MainActivityContract.View {
    constructor()

    var mPresenter: MainActivityPresenter = MainActivityPresenter(this)

    companion object {
        public val sInstance: MainFragment by lazy { MainFragment() }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d("FragmentVisible", "MainFragment visible")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_main_layout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.onCreate()
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

    }

    override fun getViewContext(): Context? = context

}