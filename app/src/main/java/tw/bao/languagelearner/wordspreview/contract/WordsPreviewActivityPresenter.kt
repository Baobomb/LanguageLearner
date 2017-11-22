package tw.bao.languagelearner.main.contract

import android.util.Log
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import tw.bao.languagelearner.model.WordDatas
import tw.bao.languagelearner.utils.db.DBDefinetion
import tw.bao.languagelearner.utils.db.DBHelper

/**
 * Created by bao on 2017/10/25.
 */
class WordsPreviewActivityPresenter(view: WordsPreviewActivityContract.View) : WordsPreviewActivityContract.Presenter {
    companion object {
        val KEY_WORD_PREVIEW_TABLE_NAME = "KEY_WORD_PREVIEW_TABLE_NAME"
    }

    var mWordsPreviewActivityView: WordsPreviewActivityContract.View = checkNotNull(view)
    var mDBHelper: DBHelper? = null

    override fun onCreate() {
        mWordsPreviewActivityView.initView()
    }

    override fun onStart() {
        doAsync {
            var wordDatas: WordDatas? = null
            mWordsPreviewActivityView.getIntent()?.getStringExtra(KEY_WORD_PREVIEW_TABLE_NAME)?.apply {
                wordDatas = prepareWords(this@apply)
            }
            uiThread {
                //TODO : set ui
                Log.d("WordDatas", "WordDatas : " + wordDatas?.type)
            }
        }
    }

    override fun onStop() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    private fun prepareWords(tableName: String): WordDatas? {
        if (mDBHelper == null) {
            mWordsPreviewActivityView.getContext()?.apply {
                mDBHelper = DBHelper(this, DBDefinetion.WORDS_DB_NAME)
            }
        }
        mDBHelper?.apply {
            return getAll(tableName)
        }
        return null
    }
}