package com.mopub.nativeads;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.flurry.android.ads.FlurryAdErrorType;
import com.flurry.android.ads.FlurryAdNative;
import com.flurry.android.ads.FlurryAdNativeAsset;
import com.flurry.android.ads.FlurryAdNativeListener;
import com.flurry.android.ads.FlurryAdTargeting;
import com.mopub.mobileads.FlurryAgentWrapper;

import java.util.ArrayList;
import java.util.List;

public class FlurryStaticNativeAd extends StaticNativeAd {

    private static final String kLogTag = FlurryStaticNativeAd.class.getSimpleName();
    private static final int IMPRESSION_VIEW_MIN_TIME = 500;
    private static final String CALL_TO_ACTION = "callToAction";

    private final Context mContext;
    private final CustomEventNative.CustomEventNativeListener mCustomEventNativeListener;
    private final FlurryStaticNativeAd mFlurryStaticNativeAd;

    private static final String ASSET_SEC_HQ_IMAGE = "secHqImage";
    private static final String ASSET_SEC_IMAGE = "secImage";
    private static final String ASSET_SEC_HQ_RATING_IMG = "secHqRatingImg";
    private static final String ASSET_SEC_BRANDING_LOGO = "secBrandingLogo";
    private static final String ASSET_SEC_HQ_BRANDING_LOGO = "secHqBrandingLogo";
    private static final String ASSET_SEC_RATING_IMG = "secRatingImg";
    private static final String ASSET_APP_RATING = "appRating";
    private static final String ASSET_APP_CATEGORY = "appCategory";
    private static final String ASSET_HEADLINE = "headline";
    private static final String ASSET_SOURCE = "source";
    private static final String ASSET_SUMMARY = "summary";
    private static final String ASSET_VIDEO = "videoUrl";
    private static final String PRIVACY_URL = "https://policies.yahoo.com/us/en/yahoo/privacy/index.html";
    private static final double MOPUB_STAR_RATING_SCALE = StaticNativeAd.MAX_STAR_RATING;

    public static final String EXTRA_STAR_RATING_IMG = "flurry_starratingimage";
    public static final String EXTRA_APP_CATEGORY = "flurry_appcategorytext";
    public static final String EXTRA_SEC_BRANDING_LOGO = "flurry_brandingimage";
    private boolean mIsUsingTestAdSource = false;

    private FlurryAdNative nativeAd;
    private Handler mHandler = new Handler();
    private String mTestGifUrl = null;

    FlurryStaticNativeAd(Context context, boolean isUsingTestAdSource, FlurryAdNative adNative,
                         CustomEventNative.CustomEventNativeListener mCustomEventNativeListener) {
        this.mContext = context;
        this.nativeAd = adNative;
        this.mCustomEventNativeListener = mCustomEventNativeListener;
        this.mFlurryStaticNativeAd = this;
        this.mIsUsingTestAdSource = isUsingTestAdSource;
    }

    public void setTestGifUrl(String testGifUrl) {
        this.mTestGifUrl = testGifUrl;
    }

    public String getTestGifUrl() {
        return mTestGifUrl;
    }

    public synchronized void fetchAd() {
        Context context = mContext;
        if (context == null) {
            return;
        }
        if (mIsUsingTestAdSource) {
            FlurryAdTargeting adTargeting = new FlurryAdTargeting();
            adTargeting.setEnableTestAds(true);
            nativeAd.setTargeting(adTargeting);
        }
        nativeAd.setListener(listener);
        nativeAd.fetchAd();
    }

    private synchronized void onFetched(FlurryAdNative adNative) {
        if (adNative != null) {
            setupNativeAd(adNative);
        }
    }

    private synchronized void onFetchFailed(FlurryAdNative adNative) {
        if (mCustomEventNativeListener != null) {
            mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_NO_FILL);
        }
    }

    private synchronized void setupNativeAd(FlurryAdNative adNative) {
        if (adNative != null) {
            nativeAd = adNative;
            FlurryAdNativeAsset coverImageAsset = nativeAd.getAsset(ASSET_SEC_HQ_IMAGE);
            FlurryAdNativeAsset iconImageAsset = nativeAd.getAsset(ASSET_SEC_IMAGE);

            if (coverImageAsset != null && !TextUtils.isEmpty(mTestGifUrl)) {
                setMainImageUrl(mTestGifUrl);
            } else if (coverImageAsset != null && !TextUtils.isEmpty(coverImageAsset.getValue())) {
                setMainImageUrl(coverImageAsset.getValue());
            }

            if (iconImageAsset != null && !TextUtils.isEmpty(iconImageAsset.getValue())) {
                setIconImageUrl(iconImageAsset.getValue());
            }

            setTitle(nativeAd.getAsset(ASSET_HEADLINE).getValue());
            //++ Jett@Gogolook
            // modify for layout info
            setText(nativeAd.getAsset(ASSET_SOURCE).getValue());
            setPrivacyInformationIconImageUrl(nativeAd == null ? null : nativeAd.getAsset(ASSET_SEC_BRANDING_LOGO).getValue());
            setPrivacyInformationIconClickThroughUrl(PRIVACY_URL);
            //addExtra(EXTRA_SEC_BRANDING_LOGO, nativeAd.getAsset(ASSET_SEC_BRANDING_LOGO).getValue());
            //-- Jett@Gogolook
            if (isAppInstallAd()) {
                // App rating image URL may be null
                FlurryAdNativeAsset ratingHqImageAsset = nativeAd.getAsset(ASSET_SEC_HQ_RATING_IMG);
                if (ratingHqImageAsset != null && !TextUtils.isEmpty(ratingHqImageAsset.getValue())) {
                    addExtra(EXTRA_STAR_RATING_IMG, ratingHqImageAsset.getValue());
                } else {
                    FlurryAdNativeAsset ratingImageAsset = nativeAd.getAsset(ASSET_SEC_RATING_IMG);
                    if (ratingImageAsset != null && !TextUtils.isEmpty(ratingImageAsset.getValue())) {
                        addExtra(EXTRA_STAR_RATING_IMG, ratingImageAsset.getValue());
                    }
                }

                FlurryAdNativeAsset appCategoryAsset = nativeAd.getAsset(ASSET_APP_CATEGORY);
                if (appCategoryAsset != null) {
                    addExtra(EXTRA_APP_CATEGORY, appCategoryAsset.getValue());
                }
                FlurryAdNativeAsset appRatingAsset = nativeAd.getAsset(ASSET_APP_RATING);
                if (appRatingAsset != null) {
                    setStarRating(getStarRatingValue(appRatingAsset.getValue()));
                }
            }

            FlurryAdNativeAsset ctaAsset = nativeAd.getAsset(CALL_TO_ACTION);
            if (ctaAsset != null) {
                setCallToAction(ctaAsset.getValue());
            }

            setImpressionMinTimeViewed(IMPRESSION_VIEW_MIN_TIME);
            mCustomEventNativeListener.onNativeAdLoaded(mFlurryStaticNativeAd);

            // remove pre-cache
//			if (getImageUrls() == null || getImageUrls().isEmpty()) {
//				Log.d(kLogTag, "preCacheImages: No images to cache. Flurry Ad Native: " + nativeAd.toString());
//				mCustomEventNativeListener.onNativeAdLoaded(mFlurryStaticNativeAd);
//			} else {
//				NativeImageHelper.preCacheImages(mContext, getImageUrls(), new NativeImageHelper.ImageListener() {
//					@Override
//					public void onImagesCached() {
//						if (mCustomEventNativeListener != null) {
//							Log.d(kLogTag, "preCacheImages: Ad image cached.");
//							mCustomEventNativeListener.onNativeAdLoaded(mFlurryStaticNativeAd);
//						} else {
//							Log.d(kLogTag, "Unable to notify cache failure: CustomEventNativeListener is null.");
//						}
//					}
//
//					@Override
//					public void onImagesFailedToCache(NativeErrorCode errorCode) {
//						if (mCustomEventNativeListener != null) {
//							Log.d(kLogTag, "preCacheImages: Unable to cache Ad image. Error[" + errorCode.toString() + "]");
//							mCustomEventNativeListener.onNativeAdFailed(errorCode);
//						} else {
//							Log.d(kLogTag, "Unable to notify cache failure: CustomEventNativeListener is null.");
//						}
//					}
//				});
//			}
        } else

        {
            Log.d(kLogTag, "Flurry Native Ad setup failed: ad object is null.");
        }

    }

    private List<String> getImageUrls() {
        final List<String> imageUrls = new ArrayList<String>(2);
        final String mainImageUrl = getMainImageUrl();

        if (mainImageUrl != null) {
            imageUrls.add(getMainImageUrl());
        }

        final String iconUrl = getIconImageUrl();
        if (iconUrl != null) {
            imageUrls.add(this.getIconImageUrl());
        }
        return imageUrls;
    }

    private Double getStarRatingValue(@Nullable String appRatingString) {
        // App rating String should be of the form X/Y. E.g. 80/100
        Double rating = null;
        if (appRatingString != null) {
            String[] ratingParts = appRatingString.split("/");
            if (ratingParts.length == 2) {
                try {
                    float numer = Integer.valueOf(ratingParts[0]);
                    float denom = Integer.valueOf(ratingParts[1]);
                    rating = (numer / denom) * MOPUB_STAR_RATING_SCALE;
                } catch (NumberFormatException e) { /*Ignore and return null*/ }
            }
        }
        return rating;
    }

    private boolean isAppInstallAd() {
        return nativeAd.getAsset(ASSET_SEC_RATING_IMG) != null || nativeAd.getAsset(ASSET_SEC_HQ_RATING_IMG) != null
                || nativeAd.getAsset(ASSET_APP_CATEGORY) != null;
    }

    public boolean isVideoAd() {
        return nativeAd.isVideoAd();
    }

    public void loadVideoIntoView(@NonNull ViewGroup videoView) {
        FlurryAdNativeAsset asset = nativeAd.getAsset(ASSET_VIDEO);
        if (asset != null) {
            asset.loadAssetIntoView(videoView);
        }
    }

    // BaseForwardingNativeAd
    @Override
    public void prepare(@NonNull final View view) {
        super.prepare(view);
        nativeAd.setTrackingView(view);
    }

    @Override
    public void clear(@NonNull View view) {
        super.clear(view);
        nativeAd.removeTrackingView();
    }

    @Override
    public void destroy() {
        super.destroy();
        nativeAd.destroy();

        // Not needed for Flurry Analytics users
        FlurryAgentWrapper.getInstance().onEndSession(mContext);
    }

    FlurryAdNativeListener listener = new FlurryAdNativeListener() {
        @Override
        public void onFetched(FlurryAdNative adNative) {
            mHandler.removeMessages(0);
            mFlurryStaticNativeAd.onFetched(adNative);

        }

        @Override
        public void onError(FlurryAdNative adNative, FlurryAdErrorType adErrorType, int errorCode) {
            mHandler.removeMessages(0);
            if (adErrorType.equals(FlurryAdErrorType.FETCH)) {
                mFlurryStaticNativeAd.onFetchFailed(adNative);
            }

        }

        @Override
        public void onShowFullscreen(FlurryAdNative adNative) {
        }

        @Override
        public void onCloseFullscreen(FlurryAdNative adNative) {
        }

        @Override
        public void onClicked(FlurryAdNative adNative) {
            notifyAdClicked();
        }

        @Override
        public void onImpressionLogged(FlurryAdNative flurryAdNative) {
            notifyAdImpressed();
        }

        @Override
        public void onAppExit(FlurryAdNative adNative) {
        }

        @Override
        public void onCollapsed(FlurryAdNative adNative) {
        }

        @Override
        public void onExpanded(FlurryAdNative adNative) {
        }
    };
}