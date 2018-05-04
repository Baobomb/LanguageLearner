package tw.bao.languagelearner

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.MobileAds
import tw.bao.languagelearner.utils.UtilsTTS
import com.flurry.android.FlurryAgent



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
        MobileAds.initialize(this, "ca-app-pub-1786708346557555~6485478925")

        FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "Q7JQNTZFTCMG4B2SP32N")
//        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713")
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