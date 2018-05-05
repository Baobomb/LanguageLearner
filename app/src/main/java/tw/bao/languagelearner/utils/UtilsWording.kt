package tw.bao.languagelearner.utils

import android.content.Context
import android.text.TextUtils
import android.util.Log

/**
 * Created by bao on 2018/5/5.
 */
object UtilsWording {

    private val LOG_TAG = UtilsWording::class.java.simpleName

    fun getSyncString(context: Context, resId: Int, vararg objects: Any): String {
        val format = getString(context, resId)
        return if (TextUtils.isEmpty(format)) "" else String.format(format!!, *objects)
    }


    fun getString(context: Context, resId: Int): String? {
        try {
            return context.getString(resId)
        } catch (e: Exception) {
            Log.d(LOG_TAG, "string id not exist")
        }
        return null
    }


}