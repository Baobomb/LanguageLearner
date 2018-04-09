package tw.bao.adsdk.debug

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * Created by chenweilun on 2017/10/20.
 */

object DebugAdUtil {

    private var sAdMobTestDeviceId: String? = null
    private var sFbTestDeviceId: String? = null

    @SuppressLint("HardwareIds")
    @JvmStatic
    fun getAdMobTestDeviceId(context: Context): String? {
        if (sAdMobTestDeviceId == null) {
            val androidId = Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID)
            val deviceId: String
            try {
                // Create MD5 Hash
                val digest = java.security.MessageDigest.getInstance("MD5")
                digest.update(androidId.toByteArray())
                val messageDigest = digest.digest()

                // Create Hex String
                val hexString = StringBuffer()
                for (i in messageDigest.indices) {
                    hexString.append(Integer.toHexString(0xFF and messageDigest[i].toInt()))
                }
                deviceId = hexString.toString()
                sAdMobTestDeviceId = deviceId
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
        }
        Log.d("AdMobTestDeviceId", "Id : " + sAdMobTestDeviceId)
        return sAdMobTestDeviceId
    }


    //Follow code use for test facebook id
    @JvmStatic
    fun getFacebookTestDeviceId(context: Context): String? {
        if (sFbTestDeviceId == null) {
            try {
                val ids = getId(context.contentResolver)
                if (!TextUtils.isEmpty(ids.androidId)) {
                    sFbTestDeviceId = a(ids.androidId)
                } else if (!TextUtils.isEmpty(ids.aid)) {
                    sFbTestDeviceId = a(ids.aid)
                } else {
                    sFbTestDeviceId = a(UUID.randomUUID().toString())
                }
            } catch (e: Exception) {

            }
        }
        Log.d("FacebookTestDeviceId", "Id : " + sFbTestDeviceId)
        return sFbTestDeviceId
    }

    private fun a(var0: String?): String? {
        var0?.apply {
            try {
                val var1 = MessageDigest.getInstance("MD5")
                return a(var1.digest(var0.toByteArray(charset("utf-8"))))
            } catch (var2: Exception) {
                return null
            }
        }
        return null
    }


    private fun a(var0: ByteArray): String {
        val var1 = StringBuilder()
        val var3 = var0.size

        for (var4 in 0 until var3) {
            val var5 = var0[var4]
            var1.append(Integer.toString((var5.toInt() and 255) + 256, 16).substring(1))
        }

        return var1.toString()
    }


    private fun getId(contentResolver: ContentResolver): IDs {
        var cursor: Cursor? = null
        var ids: IDs
        try {
            val var2 = arrayOf("aid", "androidid", "limit_tracking")
            cursor = contentResolver.query(Uri.parse("content://com.facebook.katana.provider.AttributionIdProvider"), var2, null as String?, null as Array<String>?, null as String?)
            if (cursor == null || !cursor.moveToFirst()) {
                ids = IDs(null as String?, null as String?, false)
                return ids
            }

            val aid = cursor.getString(cursor.getColumnIndex("aid"))
            val androidId = cursor.getString(cursor.getColumnIndex("androidid"))
            val isLimitTracking = java.lang.Boolean.valueOf(cursor.getString(cursor.getColumnIndex("limit_tracking")))
            return IDs(aid, androidId, isLimitTracking!!)
        } catch (var10: Exception) {
            ids = IDs(null, null, false)
        } finally {
            if (cursor != null) {
                cursor.close()
            }

        }

        return ids
    }

    private class IDs(var aid: String?, var androidId: String?, var c: Boolean)
}
