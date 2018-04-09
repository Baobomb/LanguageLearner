package com.mopub.nativeads;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.flurry.android.ads.FlurryAdNative;
import com.mopub.mobileads.FlurryAgentWrapper;

import java.util.Map;

public class FlurryCustomEventNative extends BaseCustomEventNative {

    private static final String LOGTAG = FlurryCustomEventNative.class.getSimpleName();
    private static final String FLURRY_APIKEY = "apiKey";
    private static final String FLURRY_ADSPACE = "adSpaceName";
    private static final String FLURRY_MIXED_APIKEY = "new_apiKey";
    private static final String FLURRY_MIXED_ADSPACE = "new_adSpaceName";
    private static final String FLURRY_TEST_GIF_URL_KEY = "testGifUrl";
    @Override
    protected void loadNativeAd(@NonNull final Context context,
                                @NonNull final CustomEventNativeListener customEventNativeListener,
                                @NonNull final Map<String, Object> localExtras,
                                @NonNull final Map<String, String> serverExtras) {

        final String apiKey = getValidApiKey(serverExtras);
        final String adSpace = getValidAdSpaceName(serverExtras);
        final boolean isUsingTestAdSource = isValidUsingTestAdSource(serverExtras);
        final String gifUrl = getValidTestingGifUrl(serverExtras);
        //Get the FLURRY_APIKEY and FLURRY_ADSPACE from the server.
        if (apiKey != null && adSpace != null) {
            // Not needed for Flurry Analytics users
            FlurryAgentWrapper.getInstance().onStartSession(context, apiKey);
        } else {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        final FlurryStaticNativeAd flurryStaticNativeAd =
                new FlurryStaticNativeAd(context, isUsingTestAdSource,
                        new FlurryAdNative(context, adSpace), customEventNativeListener);
        flurryStaticNativeAd.setTestGifUrl(gifUrl);
        flurryStaticNativeAd.fetchAd();
   }

    private String getValidApiKey(final Map<String, String> serverExtras) {
        final String flurryApiKey = serverExtras.get(FLURRY_APIKEY);
        final String flurryMixedApiKey = serverExtras.get(FLURRY_MIXED_APIKEY);

        if (!TextUtils.isEmpty(flurryMixedApiKey) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return flurryMixedApiKey;
        } else if (!TextUtils.isEmpty(flurryApiKey)) {
            return flurryApiKey;
        } else {
            return null;
        }
    }

    private String getValidAdSpaceName(final Map<String, String> serverExtras) {
        final String flurryAdSpace = serverExtras.get(FLURRY_ADSPACE);
        final String flurryMixedAdSpace = serverExtras.get(FLURRY_MIXED_ADSPACE);
        if (!TextUtils.isEmpty(flurryMixedAdSpace) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return flurryMixedAdSpace;
        } else if (!TextUtils.isEmpty(flurryAdSpace)) {
            return flurryAdSpace;
        } else {
            return null;
        }
    }

    private String getValidTestingGifUrl(final Map<String, String> serverExtras) {
        if (!serverExtras.containsKey(FLURRY_TEST_GIF_URL_KEY)) {
            return null;
        }

        return serverExtras.get(FLURRY_TEST_GIF_URL_KEY);
    }
}