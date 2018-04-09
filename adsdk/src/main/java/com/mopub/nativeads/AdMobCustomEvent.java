package com.mopub.nativeads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeContentAd;

import java.util.Map;

import tw.bao.adsdk.debug.DebugAdUtil;

/**
 * Created by Kenneth on 2016/7/7.
 */

public class AdMobCustomEvent extends BaseCustomEventNative {

    private static final String TAG = "AdMobCustomEvent";
    private static final String AD_UNIT_ID_KEY = "adUnitID";
    private static final String AD_CHOICE_ICON_PLACEMENT = "ad_choice_icon_placement";

    @Override
    protected void loadNativeAd(@NonNull Context context,
                                @NonNull CustomEventNativeListener customEventNativeListener,
                                @NonNull Map<String, Object> localExtras,
                                @NonNull Map<String, String> serverExtras) {

        final String adUnitID;
        final int adChoiceIconPlacement = getAdChoiceIconPlacement(serverExtras);
        final boolean isUsingTestAdSource = isValidUsingTestAdSource(serverExtras);
        if (extrasAreValid(serverExtras)) {
            adUnitID = serverExtras.get(AD_UNIT_ID_KEY);
        } else {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        if (!TextUtils.isEmpty(adUnitID)) {
            final AdMobStaticNativeAd mAdMobStaticNativeAd =
                    new AdMobStaticNativeAd(context,
                            isUsingTestAdSource,
                            adChoiceIconPlacement,
                            customEventNativeListener,
                            new ImpressionTracker(context),
                            new NativeClickHandler(context));
            mAdMobStaticNativeAd.fetchAd(adUnitID);
        }

    }

    private boolean extrasAreValid(final Map<String, String> serverExtras) {
        return (serverExtras != null) && serverExtras.containsKey(AD_UNIT_ID_KEY);
    }

    private int getAdChoiceIconPlacement(final Map<String, String> serverExtras) {
        if (serverExtras == null || !serverExtras.containsKey(AD_CHOICE_ICON_PLACEMENT)) {
            return NativeAdOptions.ADCHOICES_TOP_RIGHT;
        }
        /**adChoices placement
         * 0 -> Top left
         * 1 -> (Default) Top right
         * 2 -> bottom right
         * 3 -> bottom left
         * **/
        int placement;
        try {
            placement = Integer.parseInt(serverExtras.get(AD_CHOICE_ICON_PLACEMENT));
        } catch (Exception e) {
            placement = NativeAdOptions.ADCHOICES_TOP_RIGHT;
        }
        return (placement > 3 || placement < 0) ? NativeAdOptions.ADCHOICES_TOP_RIGHT : placement;
    }

    public class AdMobStaticNativeAd extends StaticNativeAd {

        private final Context mContext;
        private final CustomEventNative.CustomEventNativeListener mCustomEventNativeListener;
        private final AdMobStaticNativeAd mAdMobStaticNativeAd;

        private NativeAppInstallAd mNativeAppInstallAd;
        private NativeContentAd mNativeContentAd;

        private ImpressionTracker mImpressionTracker;
        private NativeClickHandler mNativeClickHandler;
        private boolean mIsUsingTestAdSource = false;
        private int mAdChoiceIconPlacement = NativeAdOptions.ADCHOICES_TOP_RIGHT;

        AdMobStaticNativeAd(Context context,
                            boolean isUsingTestAdSource,
                            int adChoiceIconPlacement,
                            CustomEventNative.CustomEventNativeListener mCustomEventNativeListener,
                            ImpressionTracker impressionTracker, NativeClickHandler nativeClickHandler) {
            this.mContext = context;
            this.mIsUsingTestAdSource = isUsingTestAdSource;
            this.mAdChoiceIconPlacement = adChoiceIconPlacement;
            this.mCustomEventNativeListener = mCustomEventNativeListener;
            this.mAdMobStaticNativeAd = this;
            this.mImpressionTracker = impressionTracker;
            this.mNativeClickHandler = nativeClickHandler;
        }

        public void fetchAd(String adUnitID) {
            if (mNativeAppInstallAd != null) {
                mNativeAppInstallAd.destroy();
                mNativeAppInstallAd = null;
            }

            if (mNativeContentAd != null) {
                mNativeContentAd.destroy();
                mNativeContentAd = null;
            }

            AdRequest.Builder builder = new AdRequest.Builder();
            if (mIsUsingTestAdSource) {
                builder.addTestDevice(DebugAdUtil.getAdMobTestDeviceId(mContext));
            }
            NativeAdOptions nativeAdOptions = new NativeAdOptions.Builder()
                    .setAdChoicesPlacement(mAdChoiceIconPlacement)
                    .build();
            AdLoader adLoader = new AdLoader.Builder(mContext, adUnitID)
                    .forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
                        @Override
                        public void onAppInstallAdLoaded(NativeAppInstallAd appInstallAd) {
                            // Show the app install ad.
                            if (mAdMobStaticNativeAd != null) {
                                mAdMobStaticNativeAd.setupNativeAd(appInstallAd);
                            }
                        }
                    })
                    .forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
                        @Override
                        public void onContentAdLoaded(NativeContentAd contentAd) {
                            // Show the content ad.
                            if (mAdMobStaticNativeAd != null) {
                                mAdMobStaticNativeAd.setupNativeAd(contentAd);
                            }
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            if (mCustomEventNativeListener != null) {
                                mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_NO_FILL);
                            }
                        }

                        @Override
                        public void onAdOpened() {
                            super.onAdOpened();
                            // If you're using an analytics package to track clickthroughs, this is a good place to record one.
                            notifyAdClicked();
                        }

                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            // If you're using a third-party analytics package to track impressions, this is also where you can place the call to record them.
                            notifyAdImpressed();
                        }
                    })
                    .withNativeAdOptions(nativeAdOptions)
                    .build();

            adLoader.loadAd(builder.build());

        }

        private void setupNativeAd(NativeContentAd contentAd) {
            if (contentAd != null && mCustomEventNativeListener != null) {
                this.mNativeContentAd = contentAd;
                mCustomEventNativeListener.onNativeAdLoaded(mAdMobStaticNativeAd);
            }
        }

        private void setupNativeAd(NativeAppInstallAd appInstallAd) {
            if (appInstallAd != null && mCustomEventNativeListener != null) {
                this.mNativeAppInstallAd = appInstallAd;
                mCustomEventNativeListener.onNativeAdLoaded(mAdMobStaticNativeAd);
            }
        }

        public NativeAppInstallAd getNativeAppInstallAd() {
            return mNativeAppInstallAd;
        }

        public NativeContentAd getNativeContentAd() {
            return mNativeContentAd;
        }

        // BaseForwardingNativeAd
        @Override
        public void prepare(@NonNull final View view) {
            super.prepare(view);
            if (mImpressionTracker != null) {
                mImpressionTracker.addView(view, this);
            }
            if (mNativeClickHandler != null) {
                mNativeClickHandler.setOnClickListener(view, this);
            }
        }

        @Override
        public void recordImpression(@NonNull View view) {
            super.recordImpression(view);
            notifyAdImpressed();
        }

        @Override
        public void handleClick(@NonNull View view) {
            this.notifyAdClicked();
        }

        @Override
        public void clear(@NonNull View view) {
            super.clear(view);
            if (mImpressionTracker != null) {
                mImpressionTracker.removeView(view);
            }
            if (mNativeClickHandler != null) {
                mNativeClickHandler.clearOnClickListener(view);
            }
        }

        @Override
        public void destroy() {
            super.destroy();
            mImpressionTracker.destroy();
            if (mNativeAppInstallAd != null) {
                mNativeAppInstallAd.destroy();
            }
            if (mNativeContentAd != null) {
                mNativeContentAd.destroy();
            }
        }
    }
}