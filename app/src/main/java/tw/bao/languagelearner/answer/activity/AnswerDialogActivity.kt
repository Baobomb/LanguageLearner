package tw.bao.languagelearner.answer.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.activity_answer_dialog_layout.*
import tw.bao.languagelearner.R
import tw.bao.languagelearner.answer.contract.AnswerDialogContract
import tw.bao.languagelearner.answer.contract.AnswerDialogPresenter
import tw.bao.languagelearner.model.WordDatas
import tw.bao.languagelearner.utils.Utils
import tw.bao.languagelearner.utils.db.DBDefinetion

/**
 * Created by bao on 2017/10/25.
 */
class AnswerDialogActivity : Activity(), AnswerDialogContract.View {

    lateinit var mPresenter: AnswerDialogPresenter
    private var mAnswerView: View? = null

    companion object {

        fun getAnswerDialogActivityIntent(context: Context): Intent {
            val ii = Intent()
            ii.setClass(context, AnswerDialogActivity::class.java)
            ii.putExtra(AnswerDialogPresenter.KEY_ANSWER_TABLE_NAME, DBDefinetion.TABLE_LIST[Utils.getRamdonInts(0, DBDefinetion.TABLE_LIST.size)[0]])
            ii.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_NO_ANIMATION or
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            return ii
        }

    }

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

    override fun showQuestionView(answerPosition: Int, wordDatas: WordDatas?) {
        setContentView(R.layout.activity_answer_dialog_layout)
        setQuestionItem(answerPosition, wordDatas)
        setAnswerItem(answerPosition, wordDatas)
    }

    private fun setQuestionItem(answerPosition: Int, wordDatas: WordDatas?) {
        wordDatas?.apply {
            mTvQuestion.text = words[answerPosition - 1]?.engWord
        }
    }

    private fun setAnswerItem(answerPosition: Int, wordDatas: WordDatas?) {
        wordDatas?.apply {
            mTvAnswerOne.setOnClickListener { showAnswer(chooseView = mTvAnswerOne) }
            mTvAnswerTwo.setOnClickListener { showAnswer(chooseView = mTvAnswerTwo) }
            mTvAnswerThird.setOnClickListener { showAnswer(chooseView = mTvAnswerThird) }
            mTvAnswerFour.setOnClickListener { showAnswer(chooseView = mTvAnswerFour) }

            mTvAnswerOne.tag = if (answerPosition == 1) ANSWER_TAG.ANSWER else ANSWER_TAG.WRONG
            mTvAnswerTwo.tag = if (answerPosition == 2) ANSWER_TAG.ANSWER else ANSWER_TAG.WRONG
            mTvAnswerThird.tag = if (answerPosition == 3) ANSWER_TAG.ANSWER else ANSWER_TAG.WRONG
            mTvAnswerFour.tag = if (answerPosition == 4) ANSWER_TAG.ANSWER else ANSWER_TAG.WRONG
            when (answerPosition) {
                1 -> mAnswerView = mTvAnswerOne
                2 -> mAnswerView = mTvAnswerTwo
                3 -> mAnswerView = mTvAnswerThird
                4 -> mAnswerView = mTvAnswerFour
            }

            mTvAnswerOne.text = words[0]?.chineseWord
            mTvAnswerTwo.text = words[1]?.chineseWord
            mTvAnswerThird.text = words[2]?.chineseWord
            mTvAnswerFour.text = words[3]?.chineseWord
        }
    }

    override fun hideQuestionView() {

    }

    override fun showAnswer(chooseView: View) {
        if (chooseView.tag == ANSWER_TAG.ANSWER) {
            chooseView.isSelected = true
            chooseView.isEnabled = true
            mPresenter.updateUserInfo(true)
        } else {
            chooseView.isSelected = true
            chooseView.isEnabled = false
            mAnswerView?.isSelected = true
            mAnswerView?.isEnabled = true
            mPresenter.updateUserInfo(false)
        }
        mTvAnswerOne.isClickable = false
        mTvAnswerTwo.isClickable = false
        mTvAnswerThird.isClickable = false
        mTvAnswerFour.isClickable = false
    }

    override fun getContext(): Context = this

    override fun getViewIntent(): Intent? = intent

    override fun stopSelf() {
        finish()
    }
}