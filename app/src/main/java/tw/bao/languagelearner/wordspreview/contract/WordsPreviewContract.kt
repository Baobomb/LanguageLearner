package tw.bao.languagelearner.main.contract

import android.content.Context
import tw.bao.languagelearner.base.BasePresenter
import tw.bao.languagelearner.base.BaseView
import tw.bao.languagelearner.model.WordDatas

/**
 * Created by bao on 2017/10/25.
 */
interface WordsPreviewContract {
    interface View : BaseView<Presenter> {
        fun getContext(): Context?
        fun setWordDatas(wordDatas: WordDatas?)
        fun setSelectedLevel(position: Int)
    }

    interface OnItemClickListener {
        fun onChineseWordSpeakerClick(chineseWords: String)
        fun onWordSpeakerClick(engWords: String)
    }

    interface Presenter : BasePresenter
}