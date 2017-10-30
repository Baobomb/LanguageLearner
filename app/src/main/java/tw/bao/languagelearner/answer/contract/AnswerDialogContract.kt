package tw.bao.languagelearner.answer.contract

import tw.bao.languagelearner.base.BasePresenter
import tw.bao.languagelearner.base.BaseView

/**
 * Created by bao on 2017/10/25.
 */
interface AnswerDialogContract {
    interface View : BaseView<Presenter> {

        fun showQuestionView()

        fun hideQuestionView()
    }

    interface Presenter : BasePresenter
}