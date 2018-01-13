package tw.bao.languagelearner

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import tw.bao.languagelearner.answer.receiver.ScreenStatusReceiver
import tw.bao.languagelearner.utils.NotifyID
import tw.bao.languagelearner.utils.UtilsNotification

/**
 * Created by bao on 2018/1/13.
 */
class MainService : Service() {
    override fun onBind(p0: Intent?): IBinder = MainServiceBinder()

    override fun onCreate() {
        super.onCreate()
        ScreenStatusReceiver.registerReceiverIfNeed()
    }

    inner class MainServiceBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        val service: MainService
            get() = this@MainService
    }


    private fun enableForeground(bEnableForeground: Boolean) {
        if (bEnableForeground) {
            val builder = UtilsNotification.getBasicBuilder(this)
                    .setContentTitle("Service is running")
                    .setContentText("Service is running")
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setDefaults(0)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
            startForeground(NotifyID.MAIN_SERVICE, UtilsNotification.build(builder))
        } else {
            stopForeground(true)
        }
    }

}