package tw.bao.languagelearner.answer.contract

import android.content.Context
import android.content.Intent
import tw.bao.languagelearner.base.BasePresenter
import tw.bao.languagelearner.base.BaseView
import tw.bao.languagelearner.model.WordDatas

/**
 * Created by bao on 2017/10/25.
 */
interface AnswerDialogContract {
    interface View : BaseView<Presenter> {

        fun showQuestionView(answerPosition: Int, wordDatas: WordDatas?)

        fun hideQuestionView()

        fun showAnswer(chooseView: android.view.View)

        fun getContext(): Context

        fun getViewIntent(): Intent?

        fun stopSelf()
    }

    interface Presenter : BasePresenter{
        fun updateUserInfo(isScored: Boolean)
    }
}