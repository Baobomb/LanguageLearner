package tw.bao.languagelearner.info.contract

import android.content.Context
import tw.bao.languagelearner.base.BasePresenter
import tw.bao.languagelearner.base.BaseView

/**
 * Created by bao on 2017/10/25.
 */
interface InfoContract {
    interface View : BaseView<Presenter> {
        fun getViewContext(): Context?
    }

    interface Presenter : BasePresenter
}