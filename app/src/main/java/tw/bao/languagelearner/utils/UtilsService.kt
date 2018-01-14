package tw.bao.languagelearner.utils

import android.app.ActivityManager
import android.content.Context
import android.text.TextUtils
import tw.bao.languagelearner.MyApplication

/**
 * Created by bao on 2018/1/13.
 */
object UtilsService {
    private val CRITERIA_ANY_RUNNING = 0
    private val CRITERIA_ALL_RUNNING = 1
    private val CRITERIA_ANY_FOREGROUND = 2
    private val CRITERIA_ALL_FOREGROUND = 3


    private fun isServiceMatchCriteria(criteria: Int, vararg serviceClasses: Class<*>): Boolean {
        if (null == serviceClasses || serviceClasses.isEmpty()) {
            return false
        }
        MyApplication.getGlobalContext() ?: return false
        var matchCount = 0
        val activityManager = MyApplication.getGlobalContext()?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            for (serviceClass in serviceClasses) {
                //                L.debug("!!!serviceClass: " + serviceClass + "   " + service.service.getClassName());
                if (TextUtils.equals(serviceClass.name, service.service.className)) {
                    if (criteria == CRITERIA_ANY_RUNNING || criteria == CRITERIA_ANY_FOREGROUND && service.foreground) {
                        return true
                    } else if (criteria == CRITERIA_ALL_RUNNING || criteria == CRITERIA_ALL_FOREGROUND && service.foreground) {
                        matchCount++
                    }
                }
            }
        }
        return matchCount == serviceClasses.size
    }

    fun isAnyRunningService(vararg serviceClasses: Class<*>): Boolean {
        return isServiceMatchCriteria(CRITERIA_ANY_RUNNING, *serviceClasses)
    }

    fun isForegroundService(vararg serviceClass: Class<*>): Boolean {
        return isServiceMatchCriteria(CRITERIA_ALL_FOREGROUND, *serviceClass)
    }

}