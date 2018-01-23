package tw.bao.languagelearner.ad

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.*
import tw.bao.languagelearner.R

/**
 * Created by bao on 2018/1/22.
 */
object AdManager {

    interface AdLoadedListener {
        fun onAdLoaded(ad: NativeAd)
    }

    public fun loadAd(context: Context, adLoadedListener: AdLoadedListener) {
        val adLoader = AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110")
                .forAppInstallAd { ad: NativeAppInstallAd ->
                    // Show the app install ad.
                    adLoadedListener.onAdLoaded(ad)
                }
                .forContentAd { ad: NativeContentAd ->
                    // Show the content ad.
                    adLoadedListener.onAdLoaded(ad)
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(errorCode: Int) {
                        Log.d("AdManager", "Error : " + errorCode)
                    }
                })
                .withNativeAdOptions(NativeAdOptions.Builder()
                        .build())
                .build()
        val adRequest = AdRequest.Builder().build()
        adLoader.loadAd(adRequest)
    }

    public fun renderAd(nativeAd: NativeAppInstallAd, parent: ViewGroup) {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater
        val adView = inflater.inflate(R.layout.inapp_sticky_ad_admob_install_ad_layout, null) as NativeAppInstallAdView
        val adImageView = adView.findViewById<ImageView>(R.id.mIvAd)
        val adTitle = adView.findViewById<TextView>(R.id.mTvAdTitle)
        val adContent = adView.findViewById<TextView>(R.id.mTvAdContent)
        val adCta = adView.findViewById<TextView>(R.id.mTvAdCta)

        adTitle.text = nativeAd.headline
        adContent.text = nativeAd.body
        nativeAd.images[0]?.apply {
            adImageView.setImageDrawable(this.drawable)
        }
        adCta.text = nativeAd.callToAction

        adView.headlineView = adTitle
        adView.bodyView = adContent
        adView.imageView = adImageView
        adView.callToActionView = adCta
        adView.setNativeAd(nativeAd)
        parent.addView(adView)
    }

    public fun renderAd(nativeAd: NativeContentAd, parent: ViewGroup) {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater
        val adView = inflater.inflate(R.layout.inapp_sticky_ad_admob_content_ad_layout, null) as NativeContentAdView
        val adImageView = adView.findViewById<ImageView>(R.id.mIvAd)
        val adTitle = adView.findViewById<TextView>(R.id.mTvAdTitle)
        val adContent = adView.findViewById<TextView>(R.id.mTvAdContent)
        val adCta = adView.findViewById<TextView>(R.id.mTvAdCta)

        adTitle.text = nativeAd.headline
        adContent.text = nativeAd.body
        nativeAd.images[0]?.apply {
            adImageView.setImageDrawable(this.drawable)
        }
        adCta.text = nativeAd.callToAction

        adView.headlineView = adTitle
        adView.bodyView = adContent
        adView.imageView = adImageView
        adView.callToActionView = adCta
        adView.setNativeAd(nativeAd)
        parent.addView(adView)
    }
}