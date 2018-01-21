package tw.bao.languagelearner

import android.app.Application
import android.content.Context
import android.util.Log
import tw.bao.languagelearner.utils.UtilsTTS

/**
 * Created by bao on 2017/11/11.
 */
class MyApplication : Application() {

    companion object {
        private var sGlobalContext: Context? = null
        fun getGlobalContext(): Context? {
            if (sGlobalContext == null) {
                Log.d("MyApplication", "Context null")
            }
            return sGlobalContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        sGlobalContext = this
        UtilsTTS.initTTSService(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        UtilsTTS.destroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        UtilsTTS.destroy()
    }
}