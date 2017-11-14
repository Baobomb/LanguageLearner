package tw.bao.languagelearner.answer.activity

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.activity_answer_dialog_layout.*
import tw.bao.languagelearner.R
import tw.bao.languagelearner.answer.contract.AnswerDialogContract
import tw.bao.languagelearner.answer.contract.AnswerDialogPresenter

/**
 * Created by bao on 2017/10/25.
 */
class AnswerDialogActivity : Activity(), AnswerDialogContract.View {

    lateinit var mPresenter: AnswerDialogPresenter
    private var mAnswerView: View? = null

    enum class ANSWER_TAG {
        ANSWER, WRONG
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = AnswerDialogPresenter(this)
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
    }

    override fun showQuestionView() {
        setContentView(R.layout.activity_answer_dialog_layout)
        mTvAnswerOne.setOnClickListener { showAnswer(chooseView = mTvAnswerOne) }
        mTvAnswerTwo.setOnClickListener { showAnswer(chooseView = mTvAnswerTwo) }
        mTvAnswerThird.setOnClickListener { showAnswer(chooseView = mTvAnswerThird) }
        mTvAnswerFour.setOnClickListener { showAnswer(chooseView = mTvAnswerFour) }
        mTvAnswerOne.tag = ANSWER_TAG.ANSWER
        mTvAnswerTwo.tag = ANSWER_TAG.WRONG
        mTvAnswerThird.tag = ANSWER_TAG.WRONG
        mTvAnswerFour.tag = ANSWER_TAG.WRONG
        mAnswerView = mTvAnswerOne
    }

    override fun hideQuestionView() {

    }

    override fun showAnswer(chooseView: View) {
        if (chooseView.tag == ANSWER_TAG.ANSWER) {
            chooseView.isSelected = true
            chooseView.isEnabled = true
        } else {
            chooseView.isSelected = true
            chooseView.isEnabled = false
            mAnswerView?.isSelected = true
            mAnswerView?.isEnabled = true
        }
    }


    override fun getContext(): Context = this
}