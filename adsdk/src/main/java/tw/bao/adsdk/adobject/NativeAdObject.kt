package tw.bao.adsdk.adobject

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.mopub.nativeads.NativeAd

/**
 * Created by chenweilun on 2017/10/13.
 */

class NativeAdObject(ad: Any, expiredTime: Int) : BaseAdObject(ad, expiredTime) {

    override fun renderAd(context: Context?, adContainer: ViewGroup?) {
        getNativeAd()?.takeIf { context != null && adContainer != null }?.apply {
            try {
                clear(adContainer!!)
                clearAd(adContainer)
                val adView = createAdView(context!!, adContainer)
                adContainer.addView(adView)
                prepare(adView)
                renderAdView(adView)
            } catch (e: Exception) {

            }
        }
    }

    override fun clearAd(adContainer: ViewGroup?) {
        adContainer?.takeIf { it.childCount > 0 }?.apply { removeAllViews() }
    }

    override fun destroy() {
        getNativeAd()?.apply {
            setMoPubNativeEventListener(null)
            destroy()
        }
        ad = null
    }

    override fun setAdEventListener(listener: AdEventListener?) {
        mAdEventListener = listener
        getNativeAd()?.setMoPubNativeEventListener(object : NativeAd.MoPubNativeEventListener {
            override fun onImpression(view: View?) {
                mAdEventListener?.onAdImpression()
            }

            override fun onClick(view: View?) {
                mAdEventListener?.onAdClick()
            }
        })
    }

    private fun getNativeAd(): NativeAd? {
        return ad?.takeIf { it is NativeAd }?.let {
            it as NativeAd
        }
    }

}
