package com.mopub.nativeads;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.ads.AdChoicesView;
import com.facebook.ads.NativeAd;
import com.gogolook.adsdk.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by carlosyang on 2017/10/3.
 */

public class SdkUtilsAdRenderer {
    private static final String TAG = SdkUtilsAdRenderer.class.getSimpleName();

    public static void setupFbAdContainer(@NonNull NativeAd nativeAd,
                                          @NonNull StaticNativeViewHolder viewHolder,
                                          @NonNull ViewGroup adChoicesContainer,
                                          @NonNull ViewGroup.LayoutParams adChoicesParams) {
        setupFbAdContainer(nativeAd, viewHolder, adChoicesContainer, adChoicesParams, true, false);
    }

    public static void setupFbAdContainer(@NonNull NativeAd nativeAd,
                                          @NonNull StaticNativeViewHolder viewHolder,
                                          @NonNull ViewGroup adChoicesContainer,
                                          @NonNull ViewGroup.LayoutParams adChoicesParams,
                                          boolean withPadding,
                                          boolean withAdChoiceIcon,
                                          @Nullable View... extraInteractViews) {
        Context context = adChoicesContainer.getContext();
        if (context != null) {
            List<View> interactViews = new ArrayList<>();
            //find all action view for interaction
            if (viewHolder.callToActionView != null) {
                interactViews.add(viewHolder.callToActionView);
            }
            if (viewHolder.iconImageView != null) {
                interactViews.add(viewHolder.iconImageView);
            }
            if (viewHolder.mainImageView != null) {
                interactViews.add(viewHolder.mainImageView);
            }
            if (viewHolder.textView != null) {
                interactViews.add(viewHolder.textView);
            }
            if (viewHolder.titleView != null) {
                interactViews.add(viewHolder.titleView);
            }
            if (extraInteractViews != null) {
                for (View view : extraInteractViews) {
                    interactViews.add(view);
                }
            }
            nativeAd.registerViewForInteraction(viewHolder.mainView, interactViews);

            AdChoicesView adChoicesView = new AdChoicesView(context, nativeAd, withAdChoiceIcon);
            if (withPadding) {
                int commonPadding = dp2px(context, 4);
                adChoicesView.setPadding(commonPadding, commonPadding, commonPadding, commonPadding);
            }
            adChoicesContainer.addView(adChoicesView);
        }
    }


    /**
     * dp轉px
     */
    public static int dp2px(@NonNull Context context, float dpValue) {
        return (int) (dpValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}
