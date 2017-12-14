package tw.bao.languagelearner.wordspreview.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tw.bao.languagelearner.R
import tw.bao.languagelearner.main.contract.WordsPreviewContract
import tw.bao.languagelearner.main.contract.WordsPreviewPresenter
import tw.bao.languagelearner.model.WordDatas
import tw.bao.languagelearner.wordspreview.adapter.WordDatasListAdapter

/**
 * Created by bao on 2017/12/9.
 */
class WordPreviewFragment : Fragment, WordsPreviewContract.View {
    constructor()

    var mPresenter: WordsPreviewPresenter = WordsPreviewPresenter(this)
    var mWordDatasAdapter: WordDatasListAdapter? = null

    companion object {
        public val sInstance: WordPreviewFragment by lazy { WordPreviewFragment() }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d("FragmentVisible", "WordPreviewFragment visible")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_words_preview_layout, container, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    }

    override fun setWordDatas(wordDatas: WordDatas?) {
        if (mWordDatasAdapter == null) {
            context?.apply {
                mWordDatasAdapter = WordDatasListAdapter(this)
            }
        }
        mWordDatasAdapter?.mWordDatas = wordDatas
    }
}