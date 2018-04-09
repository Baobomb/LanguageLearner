package tw.bao.languagelearner.wordstest.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.activity_word_test_layout.*
import tw.bao.languagelearner.R
import tw.bao.languagelearner.answer.contract.AnswerDialogPresenter
import tw.bao.languagelearner.model.WordDatas
import tw.bao.languagelearner.wordstest.contract.WordTestContract
import tw.bao.languagelearner.wordstest.contract.WordTestPresenter

/**
 * Created by bao on 2017/10/25.
 */
class WordTestActivity : Activity(), WordTestContract.View {

    lateinit var mPresenter: WordTestPresenter
    private var mAnswerView: View? = null
    private var isStart = false

    companion object {

        fun getWordTestActivityIntent(context: Context, tableName: String): Intent {
            val ii = Intent()
            ii.setClass(context, WordTestActivity::class.java)
            ii.putExtra(AnswerDialogPresenter.KEY_ANSWER_TABLE_NAME, tableName)
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
        mPresenter = WordTestPresenter(this)
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
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        setContentView(R.layout.activity_word_test_layout)
    }

    override fun showNextQuestionView(answerPosition: Int, wordDatas: WordDatas?) {
        val currentProgress = mTestProgress.progress
        Log.d("TestProgress", "Current Progress :$currentProgress")
        mTestProgress.progress = currentProgress + 1
        if (isStart) {
            fadeAnimationOuestionView(1f, 0f, object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    setQuestionItem(answerPosition, wordDatas)
                    setAnswerItem(answerPosition, wordDatas)
                    fadeAnimationOuestionView(0f, 1f, null)
                }
            })
        } else {
            fadeAnimationOuestionView(0f, 1f, object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    setQuestionItem(answerPosition, wordDatas)
                    setAnswerItem(answerPosition, wordDatas)
                }
            })
            isStart = true
        }
    }

    private fun fadeAnimationOuestionView(alphaFrom: Float, alphaTo: Float, animatorListenerAdapter: AnimatorListenerAdapter?) {
        val valueAnimator = ValueAnimator.ofFloat(alphaFrom, alphaTo)
        valueAnimator.addUpdateListener {
            val animatedValue: Float = it.animatedValue as Float
            mRlAnswerDialog.alpha = animatedValue
        }
        animatorListenerAdapter?.apply {
            valueAnimator.addListener(this)
        }
        valueAnimator.duration = 1200
        valueAnimator.start()
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
            mTvAnswerOne.isSelected = false
            mTvAnswerOne.isEnabled = true
            mTvAnswerTwo.isSelected = false
            mTvAnswerTwo.isEnabled = true
            mTvAnswerThird.isSelected = false
            mTvAnswerThird.isEnabled = true
            mTvAnswerFour.isSelected = false
            mTvAnswerFour.isEnabled = true
        }
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