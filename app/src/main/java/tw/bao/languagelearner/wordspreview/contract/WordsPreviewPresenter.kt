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
class WordsPreviewPresenter(view: WordsPreviewContract.View) : WordsPreviewContract.Presenter {
    companion object {
        val KEY_WORD_PREVIEW_TABLE_NAME = "KEY_WORD_PREVIEW_TABLE_NAME"
    }

    var mWordsPreviewView: WordsPreviewContract.View = checkNotNull(view)
    var mDBHelper: DBHelper? = null
    var dbAsync: Future<Unit>? = null
    public var mSelectedWords: String? = null

    override fun onCreate() {
    }

    fun onCreateView() {
    }

    fun onViewCreated() {
        mWordsPreviewView.initView()
    }

    override fun onStart() {
//        }
    }

    override fun onStop() {

    }

    override fun onResume() {

    }

    override fun onPause() {
        dbAsync?.cancel(true)
    }

    public fun selectWords(tableName: String) {
        mSelectedWords = tableName
        dbAsync = doAsync {
            val wordDatas = prepareWords(tableName)
            uiThread {
                //TODO : set ui
                mWordsPreviewView.setWordDatas(wordDatas)
                Log.d("WordDatas", "WordDatas : " + wordDatas?.type)
            }
        }
    }


    private fun prepareWords(tableName: String): WordDatas? {
        if (mDBHelper == null) {
            mWordsPreviewView.getContext()?.apply {
                mDBHelper = DBHelper(this, DBDefinetion.WORDS_DB_NAME)
            }
        }
        mDBHelper?.apply {
            return getAll(tableName)
        }
        return null
    }
}