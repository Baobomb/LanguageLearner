package tw.bao.languagelearner.utils

import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import tw.bao.languagelearner.MyApplication
import tw.bao.languagelearner.R

/**
 * Created by bao on 2018/1/13.
 */
object UtilsNotification {
    val NOTIFICATION_BG_COLOR = Color.parseColor("#1CBD3A")
    var sLargeIcon: Bitmap? = null

    fun getBasicBuilder(context: Context?): NotificationCompat.Builder {
        val b = NotificationCompat.Builder(context)
        b.color = NOTIFICATION_BG_COLOR
        b.setSmallIcon(context!!.applicationInfo.icon)
        b.setAutoCancel(true)


        if (context != null && (sLargeIcon == null || sLargeIcon!!.isRecycled)) {
            try {
                sLargeIcon = BitmapFactory.decodeResource(MyApplication.getGlobalContext()?.resources, R.mipmap.ic_launcher)
            } catch (e: Exception) {
            }

        }
        sLargeIcon?.takeIf { it.isRecycled }?.apply {
            b.setLargeIcon(sLargeIcon)
        }
        return b
    }

    /**
     * Prevent OOM when setLargeIcon.
     *
     * @param builder
     * @return
     */
    fun build(builder: NotificationCompat.Builder): Notification {
        return try {
            builder.build()
        } catch (t: Throwable) {
            builder.setLargeIcon(null).build()
        }

    }
}

object NotifyID {
    val MAIN_SERVICE = 1
}