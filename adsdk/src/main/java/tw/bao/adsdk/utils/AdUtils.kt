package tw.bao.adsdk.utils

import android.app.Activity
import android.content.Context
import android.os.Build

/**
 * Created by chenweilun on 2017/11/10.
 */

object AdUtils {
    @JvmStatic
    var isNeedShowAd = true

    @JvmStatic
    var defaultCtaText: String? = null
    @JvmStatic
    var sponsoredText: String? = null

    @JvmStatic
    fun checkActivityAlive(context: Context): Boolean {
        if (context is Activity) {
            var isDestroyed = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                isDestroyed = context.isDestroyed
            }
            return (!context.isFinishing
                    && !isDestroyed)
        }
        return false
    }

    @JvmStatic
    fun dp2px(context: Context, dpValue: Float): Int = (dpValue * context.applicationContext.resources.displayMetrics.density + 0.5f).toInt()
}
