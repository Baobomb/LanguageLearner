package tw.bao.languagelearner.wordspreview.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_words_preview_layout.*
import tw.bao.languagelearner.R
import tw.bao.languagelearner.info.utils.UtilsInfo
import tw.bao.languagelearner.main.contract.WordsPreviewContract
import tw.bao.languagelearner.main.contract.WordsPreviewPresenter
import tw.bao.languagelearner.model.WordDatas
import tw.bao.languagelearner.utils.db.DBDefinetion
import tw.bao.languagelearner.wordspreview.adapter.WordDatasListAdapter
import tw.bao.languagelearner.wordstest.activity.WordTestActivity

/**
 * Created by bao on 2017/12/9.
 */
class WordPreviewFragment : Fragment, WordsPreviewContract.View {
    constructor()

    var mPresenter: WordsPreviewPresenter = WordsPreviewPresenter(this)
    var mWordDatasAdapter: WordDatasListAdapter? = null

    companion object {
        private val LOG_TAG = WordPreviewFragment::class.java.simpleName
        public val sInstance: WordPreviewFragment by lazy { WordPreviewFragment() }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d(LOG_TAG, "WordPreviewFragment visible")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mPresenter.onCreateView()
        return inflater?.inflate(R.layout.fragment_words_preview_layout, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        mPresenter.onViewCreated()
        super.onViewCreated(view, savedInstanceState)
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
        Log.d(LOG_TAG, "initView")
        mRvWordsList.layoutManager = LinearLayoutManager(context)
        mTlWordsTables?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.apply {
                    mPresenter.selectWords(tag.toString())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        val userLevel = UtilsInfo.getUserLevel()
        for (tablePos in 0..userLevel) {
            val tab = mTlWordsTables.newTab()
            tab.text = "Level " + (tablePos + 1)
            val tag = DBDefinetion.TABLE_LIST[tablePos]
            tab.tag = tag
            mTlWordsTables.addTab(tab)
        }
        mPresenter.selectWords(DBDefinetion.TABLE_LIST[0])
        mIvStartTest.setOnClickListener {
            startActivity(WordTestActivity.getWordTestActivityIntent(context!!, mPresenter.mSelectedWords!!))
        }
    }

    override fun setWordDatas(wordDatas: WordDatas?) {
        if (mWordDatasAdapter == null) {
            context?.apply {
                mWordDatasAdapter = WordDatasListAdapter(this)
            }
        }
        if (mRvWordsList.adapter == null) {
            mRvWordsList.adapter = mWordDatasAdapter
        }
        mWordDatasAdapter?.mWordDatas = wordDatas
        mWordDatasAdapter?.notifyDataSetChanged()
    }
}