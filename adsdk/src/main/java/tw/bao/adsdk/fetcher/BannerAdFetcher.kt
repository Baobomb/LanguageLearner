package tw.bao.adsdk.fetcher

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import tw.bao.adsdk.Definition
import tw.bao.adsdk.Definition.AdSource.BANNER
import tw.bao.adsdk.adobject.BannerAdObject
import tw.bao.adsdk.adobject.BaseAdObject.Companion.LIVE_59_MINUTES
import tw.bao.adsdk.config.MopubConfigHelper
import tw.bao.adsdk.error.AdErrorCode.CLIENT_ERROR_MESSAGE.*
import com.mopub.mobileads.MoPubErrorCode
import com.mopub.mobileads.MoPubView

/**
 * Created by chenweilun on 2017/10/17.
 */

class BannerAdFetcher(context: Context?, adUnit: Definition.AdUnit) : BaseAdFetcher(context, adUnit) {
    private var mMopubView: MoPubView? = null

    private val mBannerAdListener = object : MoPubView.BannerAdListener {
        override fun onBannerLoaded(banner: MoPubView?) {
            banner?.apply {
                notifyAdFetchSuccess(BANNER, BannerAdObject(this, LIVE_59_MINUTES))
                return
            }
            notifyAdFetchFail(BANNER, ERROR_AD_OBJECT_INVALID.message)
        }

        override fun onBannerFailed(banner: MoPubView?, errorCode: MoPubErrorCode) {
            banner?.apply {
                bannerAdListener = null
                destroy()
            }
            notifyAdFetchFail(BANNER, errorCode.toString())
        }

        override fun onBannerClicked(banner: MoPubView) {

        }

        override fun onBannerExpanded(banner: MoPubView) {

        }

        override fun onBannerCollapsed(banner: MoPubView) {

        }
    }

    override fun startFetch() {
        if (context == null) {
            notifyAdFetchFail(BANNER, ERROR_CONTEXT_INVALID.message)
            return
        }

        val moPubAdUnitIdKey = MopubConfigHelper.getUnitId(adUnit, mIsUsingDebugAdUnit)
        if (moPubAdUnitIdKey.isNullOrEmpty()) {
            notifyAdFetchFail(BANNER, ERROR_AD_UNIT_ID_INVALID.message)
            return
        }
        mMopubView = MoPubView(context)
        mMopubView?.apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            bannerAdListener = mBannerAdListener
            autorefreshEnabled = false
            adUnitId = moPubAdUnitIdKey
            loadAd()
        }
    }

    override fun stopFetch() {
        super.stopFetch()
        mMopubView?.destroy()
        mMopubView = null
    }
}
