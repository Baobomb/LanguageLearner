package tw.bao.languagelearner.utils.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import tw.bao.languagelearner.model.WordData
import tw.bao.languagelearner.model.WordDatas
import java.util.*

/**
 * Created by bao on 2017/11/8.
 */
class DBHelper(context: Context, dbName: String, databaseVersion: Int) : SQLiteOpenHelper(context, dbName, null, databaseVersion) {
    val KEY_CHINESE_WORD = "chinese_word"
    val KEY_ENG_WORD = "eng_word"
    val KEY_ROMAN_TEXT = "roman_text"
    private val LOGTAG = DBHelper::class.java.simpleName
    private val PREFIX_CREATE_TABLE = "CREATE TABLE if not exists "
    private val PREFIX_DROP_TABLE = "DROP TABLE IF EXISTS "
    private val ID_SQL_COMMAND = "_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
    private val CHINESE_WORD_SQL_COMMAND = "chinese_word TEXT ,"
    private val ENG_WORD_SQL_COMMAND = "eng_word TEXT ,"
    private val ROMAN_TEXT_SQL_COMMAND = "roman_text TEXT ,"


    override fun onCreate(db: SQLiteDatabase) {}

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }


    fun DropDB(context: Context, dbName: String) {
        context.deleteDatabase(dbName)
    }

    fun createTable(tableName: String, command: String): Boolean {
        var isSuccess = true
        try {
            val db = this.writableDatabase
            db.execSQL("$PREFIX_CREATE_TABLE$tableName($ID_SQL_COMMAND $CHINESE_WORD_SQL_COMMAND $ENG_WORD_SQL_COMMAND $ROMAN_TEXT_SQL_COMMAND)"
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

    fun insertData(tableName: String, wordData: WordData): Boolean {
        val db = this.writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(KEY_CHINESE_WORD, wordData.chineseWord)
        values.put(KEY_ROMAN_TEXT, wordData.romanText)
        values.put(KEY_ENG_WORD, wordData.engWord)
        //        values.put(FeedContract.FeedEntry.COLUMN_NAME_TITLE, title);
        var newRowId: Long = -1

        try {
            newRowId = db.insert(
                    tableName,
                    null,
                    values)
        } catch (e: Exception) {

        }
        return newRowId > -1
    }


    fun getData(tableName: String, rowId: Int): WordData? {
        val results = ArrayList<WordData>()
        var isSuccess = true
        try {
            val db = this.getReadableDatabase()
            val query = "SELECT $KEY_CHINESE_WORD , $KEY_ENG_WORD , $KEY_ROMAN_TEXT FROM $tableName WHERE ID= $rowId"
            val cursor = db.rawQuery(query, null)
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    do {
                        val dataValue = WordData("", "", "")
                        dataValue.chineseWord = cursor.getString(cursor.getColumnIndex(KEY_CHINESE_WORD))
                        dataValue.engWord = cursor.getString(cursor.getColumnIndex(KEY_ENG_WORD))
                        dataValue.romanText = cursor.getString(cursor.getColumnIndex(KEY_ROMAN_TEXT))
                        results.add(dataValue)
                    } while (cursor.moveToNext())
                }
                cursor.close()
            }
        } catch (e: Exception) {
            isSuccess = false
        }
        return if (isSuccess && results.size > 0) results[0] else null
    }


    fun getAll(tableName: String): WordDatas? {
        val results = ArrayList<WordData>()
        var datas: WordDatas? = null
        try {
            val query = "SELECT * FROM " + tableName
            val db = this.readableDatabase
            val cursor = db.rawQuery(query, null)
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    do {
                        val dataValue = WordData("", "", "")
                        dataValue.chineseWord = cursor.getString(cursor.getColumnIndex(KEY_CHINESE_WORD))
                        dataValue.engWord = cursor.getString(cursor.getColumnIndex(KEY_ENG_WORD))
                        dataValue.romanText = cursor.getString(cursor.getColumnIndex(KEY_ROMAN_TEXT))
                        results.add(dataValue)
                    } while (cursor.moveToNext())
                }

                cursor.close()
            }
        } catch (e: Exception) {

        }

        return if (results.size > 0) WordDatas(tableName, results) else null
    }
}