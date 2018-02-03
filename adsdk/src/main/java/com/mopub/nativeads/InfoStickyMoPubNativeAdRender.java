package com.mopub.nativeads;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gogolook.adsdk.R;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.mopub.common.Preconditions;
import com.mopub.common.UrlAction;
import com.mopub.common.UrlHandler;
import com.mopub.common.VisibleForTesting;

import java.util.WeakHashMap;

import tw.bao.adsdk.utils.AdUtils;

/**
 * Created by bao on 9/25/17 and copy from kennethkao.
 */
public class InfoStickyMoPubNativeAdRender implements MoPubAdRenderer<StaticNativeAd> {
    private static final String LOG_TAG = InfoStickyMoPubNativeAdRender.class.getSimpleName();
    @NonNull
    private final ViewBinder mViewBinder;
    @VisibleForTesting
    @NonNull
    final WeakHashMap<View, StaticNativeViewHolder> mViewHolderMap;

    public InfoStickyMoPubNativeAdRender(@NonNull ViewBinder viewBinder) {
        this.mViewBinder = viewBinder;
        this.mViewHolderMap = new WeakHashMap();
    }

    @NonNull
    public View createAdView(@NonNull Context context, @Nullable ViewGroup parent) {
        return LayoutInflater.from(context).inflate(this.mViewBinder.layoutId, parent, false);
    }

    public void renderAdView(@NonNull View view, @NonNull StaticNativeAd staticNativeAd) {
        StaticNativeViewHolder staticNativeViewHolder = (StaticNativeViewHolder) this.mViewHolderMap.get(view);
        if (staticNativeViewHolder == null) {
            staticNativeViewHolder = StaticNativeViewHolder.fromViewBinder(view, this.mViewBinder);
            this.mViewHolderMap.put(view, staticNativeViewHolder);
        }

        this.update(staticNativeViewHolder, staticNativeAd);
        NativeRendererHelper.updateExtras(staticNativeViewHolder.mainView, this.mViewBinder.extras, staticNativeAd.getExtras());
        this.setViewVisibility(staticNativeViewHolder, View.VISIBLE);
    }

    public boolean supports(@NonNull BaseNativeAd nativeAd) {
        Preconditions.checkNotNull(nativeAd);
        return nativeAd instanceof StaticNativeAd;
    }

    private void update(@NonNull StaticNativeViewHolder staticNativeViewHolder, @NonNull final StaticNativeAd staticNativeAd) {
        if (staticNativeAd instanceof AdMobCustomEvent.AdMobStaticNativeAd &&
                staticNativeViewHolder.mainView.findViewById(R.id.vs_admob) != null) {

            staticNativeViewHolder.mainView.findViewById(R.id.ll_whole).setVisibility(View.GONE);
            ViewStub stub = (ViewStub) staticNativeViewHolder.mainView.findViewById(R.id.vs_admob);
            View inflatedView = null;
            if (((AdMobCustomEvent.AdMobStaticNativeAd) staticNativeAd).getNativeContentAd() != null) {
                stub.setLayoutResource(R.layout.info_sticky_ad_admob_content_ad_field);
                inflatedView = stub.inflate();
                NativeContentAd nativeContentAd = ((AdMobCustomEvent.AdMobStaticNativeAd) staticNativeAd).getNativeContentAd();
                final NativeContentAdView nativeContentAdView = ((NativeContentAdView) inflatedView.findViewById(R.id.ntadv));
                // Some assets are guaranteed to be in every NativeContentAd.
                nativeContentAdView.setHeadlineView(inflatedView.findViewById(R.id.tv_title));
                nativeContentAdView.setLogoView(inflatedView.findViewById(R.id.iv_ad));
                nativeContentAdView.setBodyView(inflatedView.findViewById(R.id.tv_content));
                nativeContentAdView.setCallToActionView(inflatedView.findViewById(R.id.tv_cta_btn));

                if (nativeContentAd.getLogo() != null) {
                    ((ImageView) nativeContentAdView.getLogoView()).setImageDrawable(nativeContentAd.getLogo().getDrawable());
                } else {
                    ((ImageView) nativeContentAdView.getLogoView()).setImageResource(R.drawable.ad_budget_green);
                }
                if (nativeContentAd.getHeadline() != null) {
                    ((TextView) nativeContentAdView.getHeadlineView()).setText(nativeContentAd.getHeadline());
                }
                if (nativeContentAd.getBody() != null) {
                    ((TextView) nativeContentAdView.getBodyView()).setText(nativeContentAd.getBody());
                }
                if (nativeContentAd.getCallToAction() != null) {
                    ((TextView) nativeContentAdView.getCallToActionView()).setText(nativeContentAd.getCallToAction());
                }
                nativeContentAdView.getCallToActionView().setVisibility(View.VISIBLE);
                // Assign native ad object to the native view.
                nativeContentAdView.setNativeAd(nativeContentAd);
                staticNativeAd.notifyAdImpressed();
            } else if (((AdMobCustomEvent.AdMobStaticNativeAd) staticNativeAd).getNativeAppInstallAd() != null) {
                stub.setLayoutResource(R.layout.info_sticky_ad_admob_install_ad_field);
                inflatedView = stub.inflate();
                NativeAppInstallAd nativeAppInstallAd = ((AdMobCustomEvent.AdMobStaticNativeAd) staticNativeAd).getNativeAppInstallAd();
                final NativeAppInstallAdView nativeAppInstallAdView = (NativeAppInstallAdView) inflatedView.findViewById(R.id.ntadv);
                nativeAppInstallAdView.setHeadlineView(inflatedView.findViewById(R.id.tv_title));
                nativeAppInstallAdView.setIconView(inflatedView.findViewById(R.id.iv_ad));
                nativeAppInstallAdView.setBodyView(inflatedView.findViewById(R.id.tv_content));
                nativeAppInstallAdView.setCallToActionView(inflatedView.findViewById(R.id.tv_cta_btn));

                if (nativeAppInstallAd.getIcon() != null) {
                    ((ImageView) nativeAppInstallAdView.getIconView()).setImageDrawable(nativeAppInstallAd.getIcon().getDrawable());
                } else {
                    ((ImageView) nativeAppInstallAdView.getIconView()).setImageResource(R.drawable.ad_budget_green);
                }
                if (nativeAppInstallAd.getHeadline() != null) {
                    ((TextView) nativeAppInstallAdView.getHeadlineView()).setText(nativeAppInstallAd.getHeadline());
                }
                if (nativeAppInstallAd.getBody() != null) {
                    ((TextView) nativeAppInstallAdView.getBodyView()).setText(nativeAppInstallAd.getBody());
                }
                if (nativeAppInstallAd.getCallToAction() != null) {
                    ((TextView) nativeAppInstallAdView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction());
                }
                nativeAppInstallAdView.getCallToActionView().setVisibility(View.VISIBLE);
                nativeAppInstallAdView.setNativeAd(nativeAppInstallAd);
                staticNativeAd.notifyAdImpressed();
            }

            if (inflatedView != null) {
                inflatedView.findViewById(R.id.iv_privacy).setVisibility(View.GONE);
                FrameLayout flFBAdChoiceContainer = (FrameLayout) inflatedView.findViewById(R.id.fl_fb_ad_choice_container);
                flFBAdChoiceContainer.setVisibility(View.GONE);
            }
            return;
        }

        NativeRendererHelper.addTextView(staticNativeViewHolder.titleView, staticNativeAd.getTitle());
        NativeRendererHelper.addTextView(staticNativeViewHolder.textView, staticNativeAd.getText());
        if (!TextUtils.isEmpty(staticNativeAd.getCallToAction())) {
            NativeRendererHelper.addTextView(staticNativeViewHolder.callToActionView, staticNativeAd.getCallToAction());
        } else {
            NativeRendererHelper.addTextView(staticNativeViewHolder.callToActionView, AdUtils.getDefaultCtaText());
        }
        if (staticNativeViewHolder.iconImageView != null) {
            Context context = staticNativeViewHolder.iconImageView.getContext();
            if (context instanceof Activity && !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && ((Activity) context).isDestroyed())) {
                Glide.with(context)
                        .load(staticNativeAd.getIconImageUrl())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(staticNativeViewHolder.iconImageView);
            }
        }

        if (staticNativeViewHolder.mainView == null) {
            return;
        }
        ImageView ivPrivacyIcon = staticNativeViewHolder.mainView.findViewById(R.id.iv_privacy);
        setAdContent(staticNativeViewHolder);
        FrameLayout flFBAdChoiceContainer = (FrameLayout) staticNativeViewHolder.mainView.findViewById(R.id.fl_fb_ad_choice_container);
        TextView tvSponsored = staticNativeViewHolder.mainView.findViewById(R.id.tv_sponsored);
        tvSponsored.setText(AdUtils.getSponsoredText());

        if (staticNativeAd instanceof FlurryStaticNativeAd) {
            addPrivacyInformationIcon(ivPrivacyIcon, R.drawable.ad_flurry_budget, staticNativeAd.getPrivacyInformationIconClickThroughUrl());
            ivPrivacyIcon.setVisibility(View.VISIBLE);
            flFBAdChoiceContainer.setVisibility(View.GONE);
        } else if (staticNativeAd instanceof FacebookNative.FacebookStaticNativeAd) {
            //right top
            SdkUtilsAdRenderer.setupFbAdContainer(
                    ((FacebookNative.FacebookStaticNativeAd) staticNativeAd).getNativeAd(),
                    staticNativeViewHolder,
                    flFBAdChoiceContainer,
                    flFBAdChoiceContainer.getLayoutParams(),
                    false,
                    true);
            ivPrivacyIcon.setVisibility(View.GONE);
            flFBAdChoiceContainer.setVisibility(View.VISIBLE);
        } else if (staticNativeAd instanceof MoPubCustomEventNative.MoPubStaticNativeAd) {
            addPrivacyInformationIcon(ivPrivacyIcon, R.drawable.ad_mopub_budget, staticNativeAd.getPrivacyInformationIconClickThroughUrl());
            ivPrivacyIcon.setVisibility(View.VISIBLE);
            flFBAdChoiceContainer.setVisibility(View.GONE);
        }
    }

    private void setAdContent(@NonNull StaticNativeViewHolder staticNativeViewHolder) {
        if (staticNativeViewHolder.callToActionView != null) {
            staticNativeViewHolder.callToActionView.setVisibility(View.VISIBLE);
        }
        if (staticNativeViewHolder.titleView != null) {
            staticNativeViewHolder.titleView.setPadding(0, 0, 0, 0);
        }
        if (staticNativeViewHolder.textView != null) {
            staticNativeViewHolder.textView.setPadding(0, 0, 0, 0);
        }
    }

    private void setViewVisibility(@NonNull StaticNativeViewHolder staticNativeViewHolder, int visibility) {
        if (staticNativeViewHolder.mainView != null) {
            staticNativeViewHolder.mainView.setVisibility(visibility);
        }
    }

    public static void addPrivacyInformationIcon(ImageView privacyInformationIconImageView, int privacyInformationImageRes, final String privacyInformationClickthroughUrl) {
        if (privacyInformationIconImageView != null) {
            if (privacyInformationClickthroughUrl == null) {
                privacyInformationIconImageView.setOnClickListener(null);
                privacyInformationIconImageView.setClickable(false);
                privacyInformationIconImageView.setImageResource(privacyInformationImageRes);
            } else {
                final Context context = privacyInformationIconImageView.getContext();
                if (context != null) {
                    privacyInformationIconImageView.setImageResource(privacyInformationImageRes);
                    privacyInformationIconImageView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            (new UrlHandler.Builder()).withSupportedUrlActions(UrlAction.IGNORE_ABOUT_SCHEME, new UrlAction[]{UrlAction.OPEN_NATIVE_BROWSER, UrlAction.OPEN_IN_APP_BROWSER, UrlAction.HANDLE_SHARE_TWEET, UrlAction.FOLLOW_DEEP_LINK_WITH_FALLBACK, UrlAction.FOLLOW_DEEP_LINK}).build().handleUrl(context, privacyInformationClickthroughUrl);
                        }
                    });
                    privacyInformationIconImageView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void destroy() {
        mViewHolderMap.clear();
    }
}
