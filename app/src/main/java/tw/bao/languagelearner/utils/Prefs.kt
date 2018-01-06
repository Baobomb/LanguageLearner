package tw.bao.languagelearner.utils

import android.content.Context
import android.content.SharedPreferences
import tw.bao.languagelearner.MyApplication

/**
 * Created by bao on 2017/12/14.
 */
object Prefs {

    private var sPref: SharedPreferences? = null
    val INDEX_PREF = "index"

    // 0 ~ 700
    public val KEY_SCORE = "user_score"
    public val KEY_TOTAL_ANSWER_NUMS = "user_total_answer_nums"
    public val KEY_ANSWER_CORRECT_NUMS = "user_answer_correct_nums"
    public val KEY_IS_ANSWER_DIALOG_OPEN = "is_answer_dialog_open"

    public fun putInt(key: String, value: Int) {
        if (sPref == null) {
            sPref = MyApplication.getGlobalContext()?.getSharedPreferences(INDEX_PREF, Context.MODE_PRIVATE)
        }
        sPref?.apply {
            edit().putInt(key, value).apply()
        }
    }

    public fun getInt(key: String, defValue: Int): Int {
        if (sPref == null) {
            sPref = MyApplication.getGlobalContext()?.getSharedPreferences(INDEX_PREF, Context.MODE_PRIVATE)
        }
        sPref?.apply {
            return getInt(key, defValue)
        }
        return defValue
    }


    public fun putBoolean(key: String, value: Boolean) {
        if (sPref == null) {
            sPref = MyApplication.getGlobalContext()?.getSharedPreferences(INDEX_PREF, Context.MODE_PRIVATE)
        }
        sPref?.apply {
            edit().putBoolean(key, value).apply()
        }
    }

    public fun getBoolean(key: String, defValue: Boolean): Boolean {
        if (sPref == null) {
            sPref = MyApplication.getGlobalContext()?.getSharedPreferences(INDEX_PREF, Context.MODE_PRIVATE)
        }
        sPref?.apply {
            return Prefs.getBoolean(key, defValue)
        }
        return defValue
    }
}