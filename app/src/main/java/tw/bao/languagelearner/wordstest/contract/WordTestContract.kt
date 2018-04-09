package tw.bao.languagelearner.wordstest.contract

import android.content.Context
import android.content.Intent
import tw.bao.languagelearner.base.BasePresenter
import tw.bao.languagelearner.base.BaseView
import tw.bao.languagelearner.model.WordDatas

/**
 * Created by bao on 2017/10/25.
 */
interface WordTestContract {
    interface View : BaseView<Presenter> {

        fun showNextQuestionView(answerPosition: Int, wordDatas: WordDatas?)

        fun showAnswer(chooseView: android.view.View)

        fun getContext(): Context

        fun getViewIntent(): Intent?

        fun showSummary()

        fun stopSelf()
    }

    interface Presenter : BasePresenter {
        fun updateUserInfo(isScored: Boolean)
        fun getAnswerSummary() : String
    }
}