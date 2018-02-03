package tw.bao.adsdk.adobject

import android.content.Context
import android.view.ViewGroup
import com.mopub.mobileads.MoPubErrorCode
import com.mopub.mobileads.MoPubView

/**
 * Created by chenweilun on 2017/10/13.
 */

class BannerAdObject(moPubView: MoPubView, expiredTime: Int) : BaseAdObject(moPubView, expiredTime) {
//    private var mAdEventListener: BaseAdObject.AdEventListener? = null

    override fun renderAd(context: Context?, adContainer: ViewGroup?) {
        getBannerAd()?.takeIf { context != null && adContainer != null }?.apply {
            clearAd(adContainer)
            adContainer?.addView(this)
            mAdEventListener?.onAdImpression()
        }
    }

    override fun clearAd(adContainer: ViewGroup?) {
        adContainer?.takeIf { it.childCount > 0 }?.apply { removeAllViews() }
    }

    override fun destroy() {
        getBannerAd()?.apply {
            bannerAdListener = null
            destroy()
        }
        ad = null
    }

    override fun setAdEventListener(listener: AdEventListener?) {
        mAdEventListener = listener
        getBannerAd()?.bannerAdListener = object : MoPubView.BannerAdListener {
            override fun onBannerLoaded(banner: MoPubView?) {

            }

            override fun onBannerFailed(banner: MoPubView?, errorCode: MoPubErrorCode?) {

            }

            override fun onBannerClicked(banner: MoPubView?) {
                mAdEventListener?.onAdClick()
            }

            override fun onBannerExpanded(banner: MoPubView?) {

            }

            override fun onBannerCollapsed(banner: MoPubView?) {

            }
        }
    }

    private fun getBannerAd(): MoPubView? {
        return ad?.takeIf { ad is MoPubView }?.let {
            it as MoPubView
        }
    }
}
