package tw.bao.languagelearner.main.contract

import android.content.Context
import tw.bao.languagelearner.base.BasePresenter
import tw.bao.languagelearner.base.BaseView

/**
 * Created by bao on 2017/10/25.
 */
interface WordsPreviewActivityContract {
    interface View : BaseView<Presenter> {
        fun getContext(): Context?
    }

    interface Presenter : BasePresenter
}