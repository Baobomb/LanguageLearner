package tw.bao.adsdk.config

import tw.bao.adsdk.Definition
import com.gogolook.adsdk.R
import com.mopub.nativeads.InfoStickyMoPubNativeAdRender
import com.mopub.nativeads.MoPubAdRenderer
import com.mopub.nativeads.RequestParameters
import com.mopub.nativeads.ViewBinder
import java.util.*

/**
 * Created by baobomb on 2017/11/08.
 */

object MopubConfigHelper {

    private val DEV_MOPUB_BLOCK_STICKY_AD_UNIT_ID: String by lazy { "b54dd15525a4430f83b356a225bed2e7" }


    private val MOPUB_BLOCK_STICKY_AD_UNIT_ID: String by lazy { "a00a388888254c268b5687ef24d13d6d" }

    fun getUnitId(adUnit: Definition.AdUnit, isDebug: Boolean): String? {
        return when (adUnit) {
            Definition.AdUnit.INFO -> if (isDebug) DEV_MOPUB_BLOCK_STICKY_AD_UNIT_ID else MOPUB_BLOCK_STICKY_AD_UNIT_ID
        }
    }

    fun getRenderer(adUnit: Definition.AdUnit): MoPubAdRenderer<*>? {
        return when (adUnit) {
            Definition.AdUnit.INFO -> {
                val viewBinder = ViewBinder.Builder(R.layout.info_sticky_ad_content_layout)
                        .iconImageId(R.id.iv_ad)
                        .titleId(R.id.tv_title)
                        .textId(R.id.tv_content)
                        .callToActionId(R.id.tv_cta_btn)
                        .build()
                InfoStickyMoPubNativeAdRender(viewBinder)
            }
        }
    }

    fun getParameters(adUnit: Definition.AdUnit): RequestParameters? {
        return when (adUnit) {
            Definition.AdUnit.INFO -> {
                val desiredAssets = EnumSet.of<RequestParameters.NativeAdAsset>(
                        RequestParameters.NativeAdAsset.TITLE,
                        RequestParameters.NativeAdAsset.TEXT,
                        RequestParameters.NativeAdAsset.MAIN_IMAGE,
                        RequestParameters.NativeAdAsset.ICON_IMAGE,
                        RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT)
                RequestParameters.Builder()
                        .desiredAssets(desiredAssets)
                        .build()
            }
        }
    }

}
