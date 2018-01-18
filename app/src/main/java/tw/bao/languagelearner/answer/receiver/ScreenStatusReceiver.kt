package tw.bao.languagelearner.answer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import tw.bao.languagelearner.MyApplication
import tw.bao.languagelearner.answer.activity.AnswerDialogActivity
import tw.bao.languagelearner.utils.Utils

/**
 * Created by bao on 2018/1/13.
 */
class ScreenStatusReceiver : BroadcastReceiver() {
    companion object {
        private val LOG_TAG = ScreenStatusReceiver::class.java.simpleName
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
        /*+ ", isCharging:" + mIsCharging*/
        when (action) {
            Intent.ACTION_SCREEN_OFF -> {
                Log.d(LOG_TAG, "screen off")

            }
            Intent.ACTION_SCREEN_ON -> {
                Log.d(LOG_TAG, "screen on")
                //TODO : show data
            }
            Intent.ACTION_USER_UNLOCKED -> {
                Log.d(LOG_TAG, "screen unlocked")
            }
            Intent.ACTION_USER_PRESENT -> {
                Log.d(LOG_TAG, "user present")
                if (Utils.showShowAnswerDialog()) {
                    Log.d(LOG_TAG, "show answer dialog")
                    context?.startActivity(AnswerDialogActivity.getAnswerDialogActivityIntent(context))
                }
            }
        }
    }
}