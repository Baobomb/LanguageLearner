package com.mopub.mobileads;

import android.text.TextUtils;

import java.util.Map;

/**
 * Created by chenweilun on 2017/10/20.
 */

public abstract class BaseCustomEventBanner extends CustomEventBanner {
    public static final String IS_NEED_USING_TEST_SOURCE_KEY = "isNeedUsingTestSource";


    public boolean isValidUsingTestAdSource(final Map<String, String> serverExtras) {
        if (!serverExtras.containsKey(IS_NEED_USING_TEST_SOURCE_KEY)) {
            return false;
        }
        final String testing = serverExtras.get(IS_NEED_USING_TEST_SOURCE_KEY);

        return !TextUtils.isEmpty(testing) && Boolean.parseBoolean(testing);
    }
}
