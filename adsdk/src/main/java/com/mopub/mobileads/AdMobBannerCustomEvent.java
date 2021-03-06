package com.mopub.mobileads;

import android.content.Context;

import tw.bao.adsdk.debug.DebugAdUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.mopub.common.util.Views;

import java.util.Map;

import static com.google.android.gms.ads.AdSize.BANNER;
import static com.google.android.gms.ads.AdSize.FULL_BANNER;
import static com.google.android.gms.ads.AdSize.LARGE_BANNER;
import static com.google.android.gms.ads.AdSize.LEADERBOARD;
import static com.google.android.gms.ads.AdSize.MEDIUM_RECTANGLE;
import static com.google.android.gms.ads.AdSize.SMART_BANNER;
import static com.google.android.gms.ads.AdSize.WIDE_SKYSCRAPER;

/**
 * Created by chenweilun on 2017/10/11.
 */

public class AdMobBannerCustomEvent extends BaseCustomEventBanner {
    /*
     * These keys are intended for MoPub internal use. Do not modify.
     */
    public static final String AD_UNIT_ID_KEY = "adUnitID";
    public static final String AD_WIDTH_KEY = "adWidth";
    public static final String AD_HEIGHT_KEY = "adHeight";
    public static final String LOCATION_KEY = "location";

    private CustomEventBanner.CustomEventBannerListener mBannerListener;
    private AdView mGoogleAdView;

    @Override
    protected void loadBanner(
            final Context context,
            final CustomEventBanner.CustomEventBannerListener customEventBannerListener,
            final Map<String, Object> localExtras,
            final Map<String, String> serverExtras) {
        mBannerListener = customEventBannerListener;
        final String adUnitId;
        final int adWidth;
        final int adHeight;
        final boolean isUsingTestAdSource;

        if (extrasAreValid(serverExtras)) {
            adUnitId = serverExtras.get(AD_UNIT_ID_KEY);
            adWidth = Integer.parseInt(serverExtras.get(AD_WIDTH_KEY));
            adHeight = Integer.parseInt(serverExtras.get(AD_HEIGHT_KEY));
            isUsingTestAdSource = isValidUsingTestAdSource(serverExtras);
        } else {
            mBannerListener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        mGoogleAdView = new AdView(context);
        mGoogleAdView.setAdListener(new AdViewListener());
        mGoogleAdView.setAdUnitId(adUnitId);

        final AdSize adSize = calculateAdSize(adWidth, adHeight);
        if (adSize == null) {
            mBannerListener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        mGoogleAdView.setAdSize(adSize);
        AdRequest.Builder builder = new AdRequest.Builder().setRequestAgent("MoPub");
        if (isUsingTestAdSource) {
            builder.addTestDevice(DebugAdUtil.getAdMobTestDeviceId(context));
        }
        final AdRequest adRequest = builder
                .build();

        try {
            mGoogleAdView.loadAd(adRequest);
        } catch (NoClassDefFoundError e) {
            // This can be thrown by Play Services on Honeycomb.
            mBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_NO_FILL);
        }
    }

    @Override
    protected void onInvalidate() {
        Views.removeFromParent(mGoogleAdView);
        if (mGoogleAdView != null) {
            mGoogleAdView.setAdListener(null);
            mGoogleAdView.destroy();
        }
    }

    private boolean extrasAreValid(Map<String, String> serverExtras) {
        try {
            Integer.parseInt(serverExtras.get(AD_WIDTH_KEY));
            Integer.parseInt(serverExtras.get(AD_HEIGHT_KEY));
        } catch (NumberFormatException e) {
            return false;
        }
        return serverExtras.containsKey(AD_UNIT_ID_KEY);
    }

    private AdSize calculateAdSize(int width, int height) {
        // Use the smallest AdSize that will properly contain the adView
        if (width <= 0 && height <= 0) {
            return SMART_BANNER;
        } else if (width == BANNER.getWidth() && height == BANNER.getHeight()) {
            return BANNER;
        } else if (width == LARGE_BANNER.getWidth() && height == LARGE_BANNER.getHeight()) {
            return LARGE_BANNER;
        } else if (width == MEDIUM_RECTANGLE.getWidth() && height == MEDIUM_RECTANGLE.getHeight()) {
            return MEDIUM_RECTANGLE;
        } else if (width == FULL_BANNER.getWidth() && height == FULL_BANNER.getHeight()) {
            return FULL_BANNER;
        } else if (width == LEADERBOARD.getWidth() && height == LEADERBOARD.getHeight()) {
            return LEADERBOARD;
        } else if (width == WIDE_SKYSCRAPER.getWidth() && height == WIDE_SKYSCRAPER.getHeight()) {
            return WIDE_SKYSCRAPER;
        } else {
            return SMART_BANNER;
        }
    }

    private class AdViewListener extends AdListener {
        /*
         * Google Play Services AdListener implementation
         */
        @Override
        public void onAdClosed() {

        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            if (mBannerListener != null) {
                mBannerListener.onBannerFailed(getMoPubErrorCode(errorCode));
            }
        }

        @Override
        public void onAdLeftApplication() {

        }

        @Override
        public void onAdLoaded() {
            if (mBannerListener != null) {
                mBannerListener.onBannerLoaded(mGoogleAdView);
            }
        }

        @Override
        public void onAdOpened() {
            if (mBannerListener != null) {
                mBannerListener.onBannerClicked();
            }
        }

        /**
         * Converts a given Google Mobile Ads SDK error code into {@link MoPubErrorCode}.
         *
         * @param error Google Mobile Ads SDK error code.
         * @return an equivalent MoPub SDK error code for the given Google Mobile Ads SDK error
         * code.
         */
        private MoPubErrorCode getMoPubErrorCode(int error) {
            MoPubErrorCode errorCode;
            switch (error) {
                case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                    errorCode = MoPubErrorCode.INTERNAL_ERROR;
                    break;
                case AdRequest.ERROR_CODE_INVALID_REQUEST:
                    errorCode = MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR;
                    break;
                case AdRequest.ERROR_CODE_NETWORK_ERROR:
                    errorCode = MoPubErrorCode.NO_CONNECTION;
                    break;
                case AdRequest.ERROR_CODE_NO_FILL:
                    errorCode = MoPubErrorCode.NO_FILL;
                    break;
                default:
                    errorCode = MoPubErrorCode.UNSPECIFIED;
            }
            return errorCode;
        }
    }

    @Deprecated
        // for testing
    AdView getGoogleAdView() {
        return mGoogleAdView;
    }
}
