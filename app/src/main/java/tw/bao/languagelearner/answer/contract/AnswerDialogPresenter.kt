package tw.bao.languagelearner.answer.contract

import android.util.Log
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import tw.bao.languagelearner.model.WordData
import tw.bao.languagelearner.model.WordDatas
import tw.bao.languagelearner.utils.Utils
import tw.bao.languagelearner.utils.db.DBDefinetion
import tw.bao.languagelearner.utils.db.DBDefinetion.WORD_TABLE_BUSINESS
import tw.bao.languagelearner.utils.db.DBHelper

/**
 * Created by bao on 2017/10/25.
 */
class AnswerDialogPresenter(view: AnswerDialogContract.View) : AnswerDialogContract.Presenter {

    var mAnswerDialogView: AnswerDialogContract.View = checkNotNull(view)
    var mDbHelper: DBHelper? = null

    override fun onCreate() {
        mAnswerDialogView.initView()
    }

    override fun onStart() {

    }

    override fun onStop() {

    }

    override fun onResume() {
        doAsync {
            val wordDatas = prepareWords(WORD_TABLE_BUSINESS)
            val counts = wordDatas?.words?.size
            val type = wordDatas?.type
            Log.d("TAG", "Type : $type Counts : $counts")
            uiThread {
                mAnswerDialogView.showQuestionView()
            }
        }
    }

    override fun onPause() {
        mAnswerDialogView.hideQuestionView()
    }

    private fun prepareWords(tableName: String): WordDatas? {
        if (mDbHelper == null) {
            mAnswerDialogView.apply {
                mDbHelper = DBHelper(mAnswerDialogView.getContext(), DBDefinetion.WORDS_DB_NAME)
            }
        }
        mDbHelper?.apply {
            val dbRows = getCount(tableName)
            return if (dbRows >= 4) {
                val positions = Utils.getRamdonInts(1, dbRows)
                val words = ArrayList<WordData?>()
                positions.mapTo(words) {
                    getData(tableName, it)
                }
                WordDatas(tableName, words.toMutableList())
            } else {
                null
            }
        }
        return null
    }
}