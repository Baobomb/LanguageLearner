package tw.bao.languagelearner.main.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.view.Window
import tw.bao.languagelearner.main.contract.AnswerDialogContract
import tw.bao.languagelearner.main.contract.AnswerDialogPresenter

/**
 * Created by bao on 2017/10/25.
 */
class AnswerDialogActivity : AppCompatActivity(), AnswerDialogContract.View {

    lateinit var mPresenter: AnswerDialogPresenter

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
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.decorView.setBackgroundColor(Color.TRANSPARENT)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mPresenter = AnswerDialogPresenter(this)
    }

    override fun showQuestionView() {

    }

    override fun hideQuestionView() {

    }
}