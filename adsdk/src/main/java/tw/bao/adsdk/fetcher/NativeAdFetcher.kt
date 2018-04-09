package tw.bao.adsdk.fetcher

import android.content.Context
import tw.bao.adsdk.Definition
import tw.bao.adsdk.Definition.AdSource.NATIVE
import tw.bao.adsdk.adobject.NativeAdObject
import tw.bao.adsdk.config.MopubConfigHelper
import tw.bao.adsdk.error.AdErrorCode.CLIENT_ERROR_MESSAGE.*
import com.mopub.nativeads.MoPubNative
import com.mopub.nativeads.NativeAd
import com.mopub.nativeads.NativeErrorCode

/**
 * Created by chenweilun on 2017/10/17.
 */

class NativeAdFetcher(context: Context?, adUnit: Definition.AdUnit) : BaseAdFetcher(context, adUnit) {
    override fun startFetch() {
        if (context == null) {
            notifyAdFetchFail(NATIVE, ERROR_CONTEXT_INVALID.message)
            return
        }

        val moPubAdUnitIdKey = MopubConfigHelper.getUnitId(adUnit, mIsUsingDebugAdUnit)
        if (moPubAdUnitIdKey.isNullOrEmpty()) {
            notifyAdFetchFail(NATIVE, ERROR_AD_UNIT_ID_INVALID.message)
            return
        }
        val moPubNative = MoPubNative(context!!, moPubAdUnitIdKey!!, object : MoPubNative.MoPubNativeNetworkListener {
            override fun onNativeLoad(nativeAd: NativeAd?) {
                if (nativeAd == null) {
                    notifyAdFetchFail(NATIVE, ERROR_AD_OBJECT_INVALID.message)
                    return
                }
                notifyAdFetchSuccess(NATIVE, NativeAdObject(nativeAd, -1))
            }

            override fun onNativeFail(errorCode: NativeErrorCode) {
                notifyAdFetchFail(NATIVE, errorCode.toString())
            }
        })
        moPubNative.registerAdRenderer(MopubConfigHelper.getRenderer(adUnit))
        moPubNative.makeRequest(MopubConfigHelper.getParameters(adUnit))
    }
}
