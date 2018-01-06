package tw.bao.languagelearner.setting.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_setting_layout.*
import tw.bao.languagelearner.R
import tw.bao.languagelearner.main.contract.SettingContract
import tw.bao.languagelearner.main.contract.SettingPresenter
import tw.bao.languagelearner.utils.Prefs

/**
 * Created by bao on 2017/12/9.
 */

class SettingFragment : Fragment, SettingContract.View {
    constructor()

    var mPresenter: SettingPresenter = SettingPresenter(this)

    companion object {
        public val sInstance: SettingFragment by lazy { SettingFragment() }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mPresenter.onCreateView()
        return inflater?.inflate(R.layout.fragment_setting_layout, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        mPresenter.onViewCreated()
        super.onViewCreated(view, savedInstanceState)
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
        mCtvAnswerDialog.setOnClickListener {
            mCtvAnswerDialog.apply {
                isChecked = !isChecked
                Prefs.putBoolean(Prefs.KEY_IS_ANSWER_DIALOG_OPEN, isChecked)
            }
        }
    }

    override fun getContext(): Context {
        return super.getContext()
    }
}