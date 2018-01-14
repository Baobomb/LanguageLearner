package tw.bao.languagelearner.answer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.TelephonyManager
import android.util.Log
import tw.bao.languagelearner.MyApplication

/**
 * Created by bao on 2018/1/13.
 */
class CallStatusReceiver : BroadcastReceiver() {
    companion object {
        private val LOG_TAG = CallStatusReceiver::class.java.simpleName
        private var sReceiver: ScreenStatusReceiver? = null
        @Synchronized
        public fun registerReceiverIfNeed() {
            if (sReceiver == null) {
                sReceiver = ScreenStatusReceiver()
                val filter = IntentFilter()
                filter.addAction(Intent.ACTION_CALL)
                filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL)
                filter.priority = IntentFilter.SYSTEM_HIGH_PRIORITY
                MyApplication.getGlobalContext()?.registerReceiver(sReceiver, filter)
            }
        }

        @Synchronized
        fun unregisterReceiver() {
            if (sReceiver != null) {
                MyApplication.getGlobalContext()?.unregisterReceiver(sReceiver)
                sReceiver = null
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) {
            return
        }
        val action = intent.action
        if (Intent.ACTION_NEW_OUTGOING_CALL == action || intent.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_RINGING) {
            //TODO : get data 電話響起
            Log.d(LOG_TAG, "out going or ringing")
        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_OFFHOOK) {
            //TODO : off hook 接起電話
            Log.d(LOG_TAG, "off hook")
        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_IDLE) {
            Log.d(LOG_TAG, "idle")
            //TODO : show data
        }

    }
}