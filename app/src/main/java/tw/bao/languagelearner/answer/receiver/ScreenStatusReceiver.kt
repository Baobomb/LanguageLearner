package tw.bao.languagelearner.answer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import tw.bao.languagelearner.MyApplication

/**
 * Created by bao on 2018/1/13.
 */
class ScreenStatusReceiver : BroadcastReceiver() {
    companion object {
        private var sReceiver: ScreenStatusReceiver? = null
        @Synchronized
        public fun registerReceiverIfNeed() {
            if (sReceiver == null) {
                sReceiver = ScreenStatusReceiver()
                val filter = IntentFilter()
                filter.addAction(Intent.ACTION_SCREEN_ON)
                filter.addAction(Intent.ACTION_SCREEN_OFF)
                filter.addAction(Intent.ACTION_USER_PRESENT)
                filter.addAction(Intent.ACTION_USER_UNLOCKED)
                filter.priority = IntentFilter.SYSTEM_HIGH_PRIORITY
                MyApplication.getGlobalContext()?.registerReceiver(sReceiver, filter)
            }
        }
    }

    override fun onReceive(p0: Context?, p1: Intent?) {

    }
}