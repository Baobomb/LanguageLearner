package tw.bao.languagelearner.main.contract

import android.util.Log
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import tw.bao.languagelearner.model.WordDatas
import tw.bao.languagelearner.utils.db.DBDefinetion
import tw.bao.languagelearner.utils.db.DBHelper
import java.util.concurrent.Future

/**
 * Created by bao on 2017/10/25.
 */
class WordsPreviewActivityPresenter(view: WordsPreviewActivityContract.View) : WordsPreviewActivityContract.Presenter {
    companion object {
        val KEY_WORD_PREVIEW_TABLE_NAME = "KEY_WORD_PREVIEW_TABLE_NAME"
    }

    var mWordsPreviewActivityView: WordsPreviewActivityContract.View = checkNotNull(view)
    var mDBHelper: DBHelper? = null
    var dbAsync: Future<Unit>? = null

    override fun onCreate() {
        mWordsPreviewActivityView.initView()
    }

    override fun onStart() {
        dbAsync = doAsync {
            var wordDatas: WordDatas? = null
            mWordsPreviewActivityView.getViewIntent()?.getStringExtra(KEY_WORD_PREVIEW_TABLE_NAME)?.apply {
                wordDatas = prepareWords(this@apply)
            }
            uiThread {
                //TODO : set ui
                mWordsPreviewActivityView.setWordDatas(wordDatas)
                Log.d("WordDatas", "WordDatas : " + wordDatas?.type)
            }
        }
    }

    override fun onStop() {

    }

    override fun onResume() {

    }

    override fun onPause() {
        dbAsync?.cancel(true)
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