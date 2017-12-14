package tw.bao.languagelearner

import android.app.Application
import android.content.Context

/**
 * Created by bao on 2017/11/11.
 */
class MyApplication : Application() {

    companion object {
        private var sGlobalContext: Context? = null
        fun getGlobalContext(): Context? {
            return sGlobalContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        sGlobalContext = this
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}