package tw.bao.languagelearner.utils.db

import android.content.Context
import tw.bao.languagelearner.model.WordData
import tw.bao.languagelearner.model.WordDatas
import tw.bao.languagelearner.utils.Utils

/**
 * Created by bao on 2017/12/21.
 */
object UtilsDB {
    fun getRandomWords(context: Context): WordData? {
        val randomWordDatasPosition = Utils.getRamdonInts(0, DBDefinetion.TABLE_LIST.size - 1)
        //TODO random table
        val wordDatas = getWordDatas(context, DBDefinetion.TABLE_LIST[randomWordDatasPosition[0]])
        wordDatas?.words?.apply {
            val randomWordsPosition: Int = Utils.getRamdonInts(0, size)[0]
            return wordDatas.words[randomWordsPosition]
        }
        return null
    }

    fun getWordDatas(context: Context, tableName: String): WordDatas? {
        val dbHelper = DBHelper(context, DBDefinetion.WORDS_DB_NAME)
        dbHelper.apply {
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