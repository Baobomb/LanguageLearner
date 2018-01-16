package tw.bao.languagelearner.utils.db

import android.content.Context
import android.util.Log
import tw.bao.languagelearner.model.WordData
import tw.bao.languagelearner.model.WordDatas
import tw.bao.languagelearner.utils.Utils

/**
 * Created by bao on 2017/12/21.
 */
object UtilsDB {
    private val LOG_TAG = UtilsDB::class.java.simpleName
    fun getRandomWords(context: Context): WordData? {
        Log.d(LOG_TAG, "getRandomWords")
        val randomWordDatasPosition = Utils.getRandomFourInts(0, DBDefinetion.TABLE_LIST.size - 1)
        Log.d(LOG_TAG, "getRandomWords randomWordDatasPosition : ${randomWordDatasPosition[0]}")
        //TODO random table
        val wordDatas = getWordDatas(context, DBDefinetion.TABLE_LIST[randomWordDatasPosition[0]])
        wordDatas?.words?.apply {
            Log.d(LOG_TAG, "getRandomWords wordDatas : $wordDatas")
            val randomWordsPosition: Int = Utils.getRandomFourInts(0, size - 1)[0]
            Log.d(LOG_TAG, "getRandomWords randomWordsPosition : $randomWordsPosition")
            return wordDatas.words[randomWordsPosition]
        }
        Log.d(LOG_TAG, "getRandomWords wordDatas : null")
        return null
    }

    fun getWordDatas(context: Context, tableName: String): WordDatas? {
        val dbHelper = DBHelper(context, DBDefinetion.WORDS_DB_NAME)
        dbHelper.apply {
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