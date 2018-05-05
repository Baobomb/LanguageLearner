package tw.bao.languagelearner

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import tw.bao.languagelearner.answer.receiver.CallStatusReceiver
import tw.bao.languagelearner.answer.receiver.ScreenStatusReceiver
import tw.bao.languagelearner.utils.*

/**
 * Created by bao on 2018/1/13.
 */
class MainService : Service() {
    companion object {
        val START = 0
        val STOP = 1

        fun changeServiceState(context: Context?, isStart: Boolean) {
            val isAlive = UtilsService.isAnyRunningService(MainService::class.java)
            val couldBeLaunch = Prefs.getBoolean(Prefs.KEY_IS_ANSWER_DIALOG_OPEN, false)
            var doChange = false
            if (isStart) {
                doChange = couldBeLaunch
            } else {
                doChange = isAlive
            }
            if (doChange && context != null) {
                val intent = Intent()
                intent.setClass(context, MainService::class.java)
                if (!isStart) {
                    context.stopService(intent)
                } else {
                    context.startService(intent)
                }
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder = MainServiceBinder()

    override fun onCreate() {
        super.onCreate()
        ScreenStatusReceiver.registerReceiverIfNeed()
        CallStatusReceiver.registerReceiverIfNeed()
    }

    override fun onDestroy() {
        super.onDestroy()
        ScreenStatusReceiver.unregisterReceiver()
        CallStatusReceiver.unregisterReceiver()
    }

    inner class MainServiceBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        val service: MainService
            get() = this@MainService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        starFunctionService()
        return Service.START_STICKY
    }

    private fun starFunctionService() {
        val builder = UtilsNotification.getBasicBuilder(this)
                .setContentTitle(UtilsWording.getString(this, R.string.app_name))
                .setContentText(UtilsWording.getString(this, R.string.service_content))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(0)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        startForeground(NotifyID.MAIN_SERVICE, UtilsNotification.build(builder))
    }

    private fun stopFunctionService() {
        stopForeground(true)
        stopSelf()
    }


}