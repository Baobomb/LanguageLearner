package tw.bao.languagelearner.utils.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import tw.bao.languagelearner.model.WordData
import tw.bao.languagelearner.model.WordDatas
import java.util.*


/**
 * Created by bao on 2017/11/8.
 */
class DBHelper(context: Context, dbName: String, databaseVersion: Int = DBDefinetion.WORDS_DB_DEFAULT_VERSION) : SQLiteOpenHelper(context, dbName, null, databaseVersion) {
    val KEY_CHINESE_WORD = "chinese_word"
    val KEY_ENG_WORD = "eng_word"
    val KEY_KR_WORD = "kr_word"
    val KEY_JAP_WORD = "jap_word"
    val KEY_ROMAN_TEXT = "roman_text"
    private val LOGTAG = DBHelper::class.java.simpleName
    private val PREFIX_CREATE_TABLE = "CREATE TABLE if not exists "
    private val PREFIX_DROP_TABLE = "DROP TABLE IF EXISTS "
    private val ID_SQL_COMMAND = "_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
    private val CHINESE_WORD_SQL_COMMAND = "chinese_word TEXT ,"
    private val ENG_WORD_SQL_COMMAND = "eng_word TEXT ,"
    private val KR_WORD_SQL_COMMAND = "kr_word TEXT ,"
    private val JAP_WORD_SQL_COMMAND = "jap_word TEXT ,"
    private val ROMAN_TEXT_SQL_COMMAND = "roman_text TEXT"


    override fun onCreate(db: SQLiteDatabase) {}

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    fun createTable(tableName: String): Boolean {
        var isSuccess = true
        try {
            val db = this.writableDatabase
            db.execSQL("$PREFIX_CREATE_TABLE$tableName($ID_SQL_COMMAND $CHINESE_WORD_SQL_COMMAND $ENG_WORD_SQL_COMMAND $KR_WORD_SQL_COMMAND $JAP_WORD_SQL_COMMAND $ROMAN_TEXT_SQL_COMMAND)"
            )
        } catch (e: Exception) {
            isSuccess = false
        }

        return isSuccess
    }

    fun dropTable(tableName: String): Boolean {
        var isSuccess = true
        try {
            val db = this.writableDatabase
            db.execSQL("$PREFIX_DROP_TABLE$tableName")
            onCreate(db)
        } catch (e: Exception) {
            isSuccess = false
        }

        return isSuccess
    }

    fun insertDatas(wordDatas: WordDatas) {
        wordDatas.words.forEach {
            insertData(wordDatas.type, it)
        }
    }

    fun insertData(tableName: String, wordData: WordData?): Boolean {
        var newRowId: Long = -1
        wordData?.apply {
            val db = this@DBHelper.writableDatabase
            val values = ContentValues()
            values.put(KEY_CHINESE_WORD, chineseWord)
            values.put(KEY_ROMAN_TEXT, romanText)
            values.put(KEY_ENG_WORD, engWord)
            values.put(KEY_KR_WORD, krWord)
            values.put(KEY_JAP_WORD, japWord)
            try {
                newRowId = db.insert(
                        tableName,
                        null,
                        values)
            } catch (e: Exception) {
                Log.d(LOGTAG, e.toString())
            }
        }
        return newRowId > -1
    }


    fun getData(tableName: String, rowId: Int): WordData? {
        val results = ArrayList<WordData>()
        var isSuccess = true
        var cursor: Cursor? = null
        try {
            val db = this.getReadableDatabase()
            val query = "SELECT $KEY_CHINESE_WORD , $KEY_ENG_WORD , $KEY_ROMAN_TEXT , $KEY_KR_WORD , $KEY_JAP_WORD FROM $tableName WHERE _id= $rowId"
            cursor = db.rawQuery(query, null)
            cursor?.apply {
                if (moveToFirst()) {
                    do {
                        val dataValue = WordData("", "", "", "", "")
                        dataValue.chineseWord = getString(getColumnIndex(KEY_CHINESE_WORD))
                        dataValue.engWord = getString(getColumnIndex(KEY_ENG_WORD))
                        dataValue.romanText = getString(getColumnIndex(KEY_ROMAN_TEXT))
                        dataValue.krWord = getString(getColumnIndex(KEY_KR_WORD))
                        dataValue.japWord = getString(getColumnIndex(KEY_JAP_WORD))
                        results.add(dataValue)
                    } while (moveToNext())
                }
            }
        } catch (e: Exception) {
            isSuccess = false
        } finally {
            cursor?.close()
        }
        return if (isSuccess && results.size > 0) results[0] else null
    }


    fun getAll(tableName: String): WordDatas? {
        val results = ArrayList<WordData>()
        var datas: WordDatas? = null
        var cursor: Cursor? = null
        try {
            val query = "SELECT * FROM " + tableName
            val db = this.readableDatabase
            cursor = db.rawQuery(query, null)
            cursor?.apply {
                if (moveToFirst()) {
                    do {
                        val dataValue = WordData("", "", "", "", "")
                        dataValue.chineseWord = getString(getColumnIndex(KEY_CHINESE_WORD))
                        dataValue.engWord = getString(getColumnIndex(KEY_ENG_WORD))
                        dataValue.romanText = getString(getColumnIndex(KEY_ROMAN_TEXT))
                        dataValue.krWord = getString(getColumnIndex(KEY_KR_WORD))
                        dataValue.japWord = getString(getColumnIndex(KEY_JAP_WORD))
                        results.add(dataValue)
                    } while (moveToNext())
                }
            }
        } catch (e: Exception) {

        } finally {
            cursor?.close()
        }

        return if (results.size > 0) WordDatas(tableName, results.toMutableList()) else null
    }

    fun getCount(tableName: String): Int {
        var cnt = 0
        var cursor: Cursor? = null
        try {
            val countQuery = "SELECT  * FROM $tableName"
            val db = this.readableDatabase
            cursor = db.rawQuery(countQuery, null)
            cnt = cursor.count
        } catch (exception: Exception) {

        } finally {
            cursor?.close()
        }
        return cnt
    }
}