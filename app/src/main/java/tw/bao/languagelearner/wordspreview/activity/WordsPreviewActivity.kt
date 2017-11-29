package tw.bao.languagelearner.main.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import tw.bao.languagelearner.R
import tw.bao.languagelearner.main.contract.WordsPreviewActivityContract
import tw.bao.languagelearner.main.contract.WordsPreviewActivityPresenter
import tw.bao.languagelearner.model.WordDatas
import tw.bao.languagelearner.wordspreview.adapter.WordDatasListAdapter

/**
 * Created by bao on 2017/10/25.
 */
class WordsPreviewActivity : AppCompatActivity(), WordsPreviewActivityContract.View {

    lateinit var mPresenter: WordsPreviewActivityPresenter
    var mWordDatasAdapter: WordDatasListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = WordsPreviewActivityPresenter(this)
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
        setContentView(R.layout.activity_words_preview_layout)
    }

    override fun setWordDatas(wordDatas: WordDatas?) {
        if (mWordDatasAdapter == null) {
            mWordDatasAdapter = WordDatasListAdapter(this)
        }
        mWordDatasAdapter?.mWordDatas = wordDatas
    }

    override fun getContext(): Context? = this

    override fun getViewIntent(): Intent {
        return intent
    }

}