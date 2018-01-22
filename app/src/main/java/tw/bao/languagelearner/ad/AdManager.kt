package tw.bao.languagelearner.ad

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.formats.*
import tw.bao.languagelearner.R

/**
 * Created by bao on 2018/1/22.
 */
object AdManager {
    interface AdLoadedListener {
        fun onAdLoaded(ad: NativeAd)
    }

    public fun loadAd(context: Context) {
        val adLoader = AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110")
                .forAppInstallAd { ad: NativeAppInstallAd ->
                    // Show the app install ad.
                }
                .forContentAd { ad: NativeContentAd ->
                    // Show the content ad.
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(errorCode: Int) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build()
    }

    private fun renderAd(nativeAd: NativeAppInstallAd, parent: ViewGroup) {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater
        val adView = inflater.inflate(R.layout.admob_install_ad_layout, parent) as NativeAppInstallAdView

        // Locate the view that will hold the headline, set its text, and use the
        // NativeAppInstallAdView's headlineView property to register it.
//        val headlineView = adView.findViewById<TextView>(R.id.ad_headline)
//        headlineView.text = nativeAd.headline
//        adView.headlineView = headlineView


        // Call the NativeAppInstallAdView's setNativeAd method to register the
        // NativeAdObject.
        adView.setNativeAd(nativeAd)

        // Place the AdView into the parent.
        parent.addView(adView)
    }

    private fun renderAd(nativeAd: NativeContentAd, parent: ViewGroup) {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater
        val adView = inflater.inflate(R.layout.admob_content_ad_layout, parent) as NativeContentAdView

        // Locate the view that will hold the headline, set its text, and use the
        // NativeAppInstallAdView's headlineView property to register it.
//        val headlineView = adView.findViewById<TextView>(R.id.ad_headline)
//        headlineView.text = nativeAd.headline
//        adView.headlineView = headlineView


        // Call the NativeAppInstallAdView's setNativeAd method to register the
        // NativeAdObject.
        adView.setNativeAd(nativeAd)

        // Place the AdView into the parent.
        parent.addView(adView)
    }
}