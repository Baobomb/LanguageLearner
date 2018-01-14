package tw.bao.languagelearner.utils

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import tw.bao.languagelearner.MyApplication
import java.util.*

/**
 * Created by bao on 2018/1/14.
 */
object UtilsPermission {

    public val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_PHONE_STATE)
    private val sPermissions = Hashtable<String, Int>()
    public val REQUEST_PERMISSION_CODE = 1985


    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!sPermissions.containsKey(permission) || sPermissions[permission] == PackageManager.PERMISSION_DENIED) {
                val context = MyApplication.getGlobalContext()
                val permissionState = context?.checkSelfPermission(permission)
                permissionState?.apply {
                    sPermissions.put(permission, this)
                }
            }
            sPermissions[permission] == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    /**
     * Does the app have all the specified permissions
     */
    fun hasPermissions(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (!hasPermission(permission)) {
                return false
            }
        }
        return true
    }

    fun hasRequiredPermissions(): Boolean {
        return hasPermission(Manifest.permission.READ_PHONE_STATE)
    }


    @TargetApi(Build.VERSION_CODES.M)
    fun requestRequiredPermissions(activity: Activity, requestCode: Int) {
        val missingPermissions = REQUIRED_PERMISSIONS
        if (missingPermissions.isEmpty()) {
            return
        }
        android.support.v4.app.ActivityCompat.requestPermissions(activity, missingPermissions, requestCode)
    }


}