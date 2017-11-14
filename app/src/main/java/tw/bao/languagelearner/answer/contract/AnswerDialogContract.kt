package tw.bao.languagelearner.answer.contract

import android.content.Context
import tw.bao.languagelearner.base.BasePresenter
import tw.bao.languagelearner.base.BaseView

/**
 * Created by bao on 2017/10/25.
 */
interface AnswerDialogContract {
    interface View : BaseView<Presenter> {

        fun showQuestionView()

        fun hideQuestionView()

        fun showAnswer(chooseView: android.view.View)

        fun getContext(): Context
    }

    interface Presenter : BasePresenter
}