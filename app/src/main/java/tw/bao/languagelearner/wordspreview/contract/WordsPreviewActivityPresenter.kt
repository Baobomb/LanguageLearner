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

    var mWordsPreviewActivityView: WordsPreviewActivityContract.View = checkNotNull(view)
    var mDBHelper: DBHelper? = null

    override fun onCreate() {
        mWordsPreviewActivityView.initView()
    }

    override fun onStart() {
        doAsync {
            val wordDatas: WordDatas? = prepareWords(DBDefinetion.WORD_TABLE_BUSINESS)
            Log.d("WordDatas", "WordDatas : " + wordDatas?.type)
            uiThread {
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