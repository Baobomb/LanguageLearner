package tw.bao.languagelearner.main.contract

import tw.bao.languagelearner.base.BasePresenter
import tw.bao.languagelearner.base.BaseView

/**
 * Created by bao on 2017/10/25.
 */
interface MainActivityContract {
    interface View : BaseView<Presenter> {
    }

    interface Presenter : BasePresenter
}