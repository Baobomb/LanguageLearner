package tw.bao.languagelearner.answer.contract

import android.util.Log
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import tw.bao.languagelearner.info.utils.UtilsInfo
import tw.bao.languagelearner.model.WordData
import tw.bao.languagelearner.model.WordDatas
import tw.bao.languagelearner.utils.Utils
import tw.bao.languagelearner.utils.db.DBDefinetion
import tw.bao.languagelearner.utils.db.DBHelper

/**
 * Created by bao on 2017/10/25.
 */
class AnswerDialogPresenter(view: AnswerDialogContract.View) : AnswerDialogContract.Presenter {
    companion object {
        val KEY_ANSWER_TABLE_NAME = "KEY_ANSWER_TABLE_NAME"
    }


    var mAnswerDialogView: AnswerDialogContract.View = checkNotNull(view)
    var mDbHelper: DBHelper? = null

    override fun onCreate() {
        mAnswerDialogView.initView()
    }

    override fun onStart() {
    }

    override fun onStop() {
        mAnswerDialogView.stopSelf()
    }

    override fun onResume() {
        val tableName = mAnswerDialogView.getViewIntent()?.getStringExtra(KEY_ANSWER_TABLE_NAME)
        if (tableName != null) {
            doAsync {
                //TODO : Get Table name
                val wordDatas = prepareWords(tableName)
                val counts = wordDatas?.words?.size
                val type = wordDatas?.type
                Log.d("TAG", "Type : $type Counts : $counts")
                Log.d("TAG", "words size : " + wordDatas?.words?.size)
                val answerPosition = Utils.getRandomFourInts(1, 4)[0]
                uiThread {
                    mAnswerDialogView.showQuestionView(answerPosition = answerPosition, wordDatas = wordDatas)
                }
            }
        } else {
            mAnswerDialogView.stopSelf()
        }
    }

    override fun onPause() {
        mAnswerDialogView.hideQuestionView()
    }

    override fun updateUserInfo(isScored: Boolean) {
        if (isScored) {
            UtilsInfo.upUserScore()
        }
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
                val positions = Utils.getRandomFourInts(1, dbRows)
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