package tw.bao.adsdk.adobject

import android.content.Context
import android.view.ViewGroup
import java.util.*

/**
 * Created by chenweilun on 2017/10/13.
 */

abstract class BaseAdObject(ad: Any?, expiredTime: Int) {
    var ad: Any? = null
    var mAdEventListener: AdEventListener? = null
    private var mDateOfExpiration: java.util.Date? = null

    val isAdFetched: Boolean
        get() = ad != null

    val isExpired: Boolean
        get() = mDateOfExpiration != null && mDateOfExpiration!!.before(java.util.Date())

    interface AdEventListener {
        fun onAdImpression()

        fun onAdClick()
    }

    init {
        this.ad = ad
        setExpirationTime(expiredTime)
    }

    private fun setExpirationTime(expiredTime: Int) {
        if (expiredTime < 0) {
            mDateOfExpiration = null
            return
        }
        mDateOfExpiration = java.util.Date()
        val cal = java.util.Calendar.getInstance()
        cal.time = mDateOfExpiration
        cal.add(Calendar.MINUTE, if (expiredTime < LIVE_59_MINUTES) LIVE_59_MINUTES else expiredTime)
        mDateOfExpiration = cal.time
    }

    abstract fun renderAd(context: Context?, adContainer: ViewGroup?)

    abstract fun clearAd(adContainer: ViewGroup?)

    abstract fun destroy()

    abstract fun setAdEventListener(listener: AdEventListener?)

    companion object {
        val LIVE_59_MINUTES = 59
    }
}
