package com.mopub.nativeads;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAd.Rating;
import tw.bao.adsdk.debug.DebugAdUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Tested with Facebook SDK 4.8.1. FacebookAdRenderer is also necessary in order to show video ads.
 * Video ads will only be shown if VIDEO_ENABLED is set to true or a server configuration
 * "video_enabled" flag is set to true. The server configuration will override the local
 * configuration.
 */
public class FacebookNative extends BaseCustomEventNative {
    private static final String LOGTAG = FacebookNative.class.getSimpleName();
    private static final String PLACEMENT_ID_KEY = "placement_id";
    private static final String MIXED_PLACEMENT_ID_KEY = "new_placement_id";
    private static final String VIDEO_ENABLED_KEY = "video_enabled";
    private static final String FB_TEST_HASH_ID_KEY = "fb_test_hash_id";

    // CustomEventNative implementation
    @Override
    protected void loadNativeAd(final Context context,
                                final CustomEventNativeListener customEventNativeListener,
                                final Map<String, Object> localExtras,
                                final Map<String, String> serverExtras) {

        final boolean videoEnable = isVideoEnabled(serverExtras);
        final String placementId = getValidPlacementId(serverExtras, videoEnable);
        final boolean isUsingTestAdSource = isValidUsingTestAdSource(serverExtras);

        if (placementId == null) {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        final FacebookStaticNativeAd facebookStaticNativeAd = new FacebookStaticNativeAd(
                context,
                videoEnable,
                isUsingTestAdSource,
                new NativeAd(context, placementId),
                customEventNativeListener);
        facebookStaticNativeAd.loadAd();
    }

    private static boolean isVideoEnabled(Map<String, String> serverExtras) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        final String videoEnabledString = serverExtras.get(VIDEO_ENABLED_KEY);
        final boolean videoEnabledFromServer = Boolean.parseBoolean(videoEnabledString);
        return (TextUtils.isEmpty(videoEnabledString) || videoEnabledFromServer);
    }


    private String getValidPlacementId(final Map<String, String> serverExtras, final boolean videoEnable) {
        final String placementId = serverExtras.get(PLACEMENT_ID_KEY);
        final String mixed_placementId = serverExtras.get(MIXED_PLACEMENT_ID_KEY);
        if (!TextUtils.isEmpty(mixed_placementId) && videoEnable) {
            return mixed_placementId;
        } else if (!TextUtils.isEmpty(placementId)) {
            return placementId;
        } else {
            return null;
        }
    }

    static class FacebookStaticNativeAd extends StaticNativeAd implements AdListener {
        private static final String SOCIAL_CONTEXT_FOR_AD = "socialContextForAd";

        private final Context mContext;
        @NonNull
        private final NativeAd mNativeAd;
        private final CustomEventNativeListener mCustomEventNativeListener;
        private boolean mIsVideoAdEnable = false;
        private MediaView mMediaView = null;
        private boolean mIsUsingTestAdSource = false;

        FacebookStaticNativeAd(final Context context,
                               final Boolean isVideoAdEnable,
                               final Boolean isUsingTestAdSource,
                               final NativeAd nativeAd,
                               final CustomEventNativeListener customEventNativeListener) {
            mContext = context.getApplicationContext();
            mNativeAd = nativeAd;
            mIsVideoAdEnable = isVideoAdEnable;
            mIsUsingTestAdSource = isUsingTestAdSource;
            mCustomEventNativeListener = customEventNativeListener;
        }

        public boolean isVideoAdEnable() {
            return mIsVideoAdEnable;
        }

        void loadAd() {
            if (mIsUsingTestAdSource) {
                try {
                    AdSettings.addTestDevice(DebugAdUtil.getFacebookTestDeviceId(mContext));
                } catch (Exception e) {

                }
            }
            mNativeAd.setAdListener(this);
            mNativeAd.loadAd();
        }

        public void loadVideoAd(final MediaView mediaView) {
            if (mMediaView != null) {
                mMediaView.destroy();
                mMediaView = null;
            }

            if (mediaView != null) {
                this.mMediaView = mediaView;
                mMediaView.setNativeAd(mNativeAd);
            }
        }

        @NonNull
        public NativeAd getNativeAd() {
            return mNativeAd;
        }

        // AdListener
        @Override
        public void onAdLoaded(final Ad ad) {
            // This identity check is from Facebook's Native API sample code:
            // https://developers.facebook.com/docs/audience-network/android/native-api
            if (!mNativeAd.equals(ad) || !mNativeAd.isAdLoaded()) {
                mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_INVALID_STATE);
                return;
            }

            setTitle(mNativeAd.getAdTitle());
            setText(mNativeAd.getAdBody());

            final NativeAd.Image coverImage = mNativeAd.getAdCoverImage();
            setMainImageUrl(coverImage == null ? null : coverImage.getUrl());

            final NativeAd.Image icon = mNativeAd.getAdIcon();
            setIconImageUrl(icon == null ? null : icon.getUrl());

            setCallToAction(mNativeAd.getAdCallToAction());
            setStarRating(getDoubleRating(mNativeAd.getAdStarRating()));

            addExtra(SOCIAL_CONTEXT_FOR_AD, mNativeAd.getAdSocialContext());

            final NativeAd.Image adChoicesIconImage = mNativeAd.getAdChoicesIcon();
            setPrivacyInformationIconImageUrl(adChoicesIconImage == null ? null : adChoicesIconImage
                    .getUrl());
            setPrivacyInformationIconClickThroughUrl(mNativeAd.getAdChoicesLinkUrl());

            final List<String> imageUrls = new ArrayList<String>();
            final String mainImageUrl = getMainImageUrl();
            if (mainImageUrl != null) {
                imageUrls.add(getMainImageUrl());
            }
            final String iconUrl = getIconImageUrl();
            if (iconUrl != null) {
                imageUrls.add(getIconImageUrl());
            }
            final String privacyInformationIconImageUrl = getPrivacyInformationIconImageUrl();
            if (privacyInformationIconImageUrl != null) {
                imageUrls.add(privacyInformationIconImageUrl);
            }
            mCustomEventNativeListener.onNativeAdLoaded(FacebookStaticNativeAd.this);
//			preCacheImages(mContext, imageUrls, new NativeImageHelper.ImageListener() {
//				@Override
//				public void onImagesCached() {
//					mCustomEventNativeListener.onNativeAdLoaded(FacebookStaticNativeAd.this);
//				}
//
//				@Override
//				public void onImagesFailedToCache(NativeErrorCode errorCode) {
//					mCustomEventNativeListener.onNativeAdFailed(errorCode);
//				}
//			});
        }

        @Override
        public void onError(final Ad ad, final AdError adError) {
            if (adError == null) {
                mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
            } else if (adError.getErrorCode() == AdError.NO_FILL.getErrorCode()) {
                mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_NO_FILL);
            } else if (adError.getErrorCode() == AdError.INTERNAL_ERROR.getErrorCode()) {
                mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_INVALID_STATE);
            } else {
                mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
            }
        }

        @Override
        public void onAdClicked(final Ad ad) {
            notifyAdClicked();
        }

        // ImpressionListener
        @Override
        public void onLoggingImpression(final Ad ad) {
            notifyAdImpressed();
        }

        // BaseForwardingNativeAd
        @Override
        public void prepare(@NonNull final View view) {
            //Don't need to registerViewForInteraction here for now
            //NativeAd will register after rendering
//            mNativeAd.registerViewForInteraction(view);
        }

        @Override
        public void clear(@NonNull final View view) {
            mNativeAd.unregisterView();
        }

        @Override
        public void destroy() {
            super.destroy();
            if (mMediaView != null) {
                mMediaView.destroy();
                mMediaView = null;
            }

            mNativeAd.destroy();
        }

        private Double getDoubleRating(final Rating rating) {
            if (rating == null) {
                return null;
            }

            return MAX_STAR_RATING * rating.getValue() / rating.getScale();
        }
    }
}