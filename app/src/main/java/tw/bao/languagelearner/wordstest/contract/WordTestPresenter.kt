package tw.bao.languagelearner.wordstest.contract

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
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
class WordTestPresenter(view: WordTestContract.View) : WordTestContract.Presenter {
    companion object {
        val KEY_ANSWER_TABLE_NAME = "KEY_ANSWER_TABLE_NAME"
    }

    var mView: WordTestContract.View = checkNotNull(view)
    var mDbHelper: DBHelper? = null
    var mWordDatas: WordDatas? = null
    var mNowAnswerStartPosition = 0

    override fun onCreate() {
        mView.initView()
    }

    override fun onStart() {

    }

    override fun onStop() {

    }

    override fun onResume() {
        val tableName = mView.getViewIntent()?.getStringExtra(KEY_ANSWER_TABLE_NAME)
        if (tableName != null) {
            doAsync {
                //TODO : Get Table name
                mWordDatas = prepareWords(tableName)
                val counts = mWordDatas?.words?.size
                val type = mWordDatas?.type
                Log.d("TAG", "Type : $type Counts : $counts")
                uiThread {
                    tryToShowNextQuestion()
                }
            }
        } else {
            mView.stopSelf()
        }
    }

    override fun onPause() {

    }

    override fun updateUserInfo(isScored: Boolean) {
        if (isScored) {
            UtilsInfo.upUserScore()
        }
        val timer = ValueAnimator.ofInt(0, 1)
        timer.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                tryToShowNextQuestion()
            }
        })
        timer.duration = 2000
        timer.start()
    }

    private fun tryToShowNextQuestion() {
        if (mWordDatas == null || mWordDatas?.words == null || mNowAnswerStartPosition >= mWordDatas!!.words.size) {
            mView.stopSelf()
            return
        }
        val words = ArrayList<WordData?>()
        val positions = arrayOf(mNowAnswerStartPosition, mNowAnswerStartPosition + 1, mNowAnswerStartPosition + 2, mNowAnswerStartPosition + 3)
        positions.mapTo(words) {
            mWordDatas!!.words[it]
        }
        val answerPosition = Utils.getRandomFourInts(1, 4)[0]
        Log.d("WORDS_TEST", "Now answer position : " + mNowAnswerStartPosition)
        mNowAnswerStartPosition += 4
        Log.d("WORDS_TEST", "Next answer position : " + mNowAnswerStartPosition)
        mView.showNextQuestionView(answerPosition = answerPosition, wordDatas = mWordDatas)
    }

    private fun prepareWords(tableName: String): WordDatas? {
        if (mDbHelper == null) {
            mView.apply {
                mDbHelper = DBHelper(mView.getContext(), DBDefinetion.WORDS_DB_NAME)
            }
        }
        mDbHelper?.apply {
            val dbRows = getCount(tableName)
            return if (dbRows >= 4) {
                val positions = Utils.getRandomInts(1, dbRows)
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