package com.gogolook.adsdk.cache

import tw.bao.adsdk.Definition
import tw.bao.adsdk.adobject.BannerAdObject
import tw.bao.adsdk.adobject.BaseAdObject
import tw.bao.adsdk.adobject.NativeAdObject
import com.mopub.mobileads.MoPubView
import gogolook.callgogolook2.mock.BaseTestCase
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import tw.bao.adsdk.cache.AdCacheManager

/**
 * Created by chenweilun on 2017/12/4.
 */

@PrepareForTest(
        AdCacheManager::class, BaseAdObject::class, MoPubView::class
)
@RunWith(PowerMockRunner::class)
class AdCacheManagerTest : BaseTestCase() {
    lateinit var mMockNativeAdObject: NativeAdObject
    lateinit var mMockBannerAdObject: BannerAdObject
    lateinit var mMockMoPubView: MoPubView

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        mMockNativeAdObject = NativeAdObject(Any(), -1)
        mMockMoPubView = PowerMockito.mock(MoPubView::class.java)
        mMockBannerAdObject = BannerAdObject(mMockMoPubView, -1)
    }

    @Before
    @Throws(Exception::class)
    override fun tearDown() {
        super.tearDown()
    }

    @Test
    fun testHasCache() {
        AdCacheManager.clearCache()
        Assert.assertFalse(AdCacheManager.hasCache(Definition.AdUnit.INFO))
        AdCacheManager.putCacheAd(Definition.AdUnit.INFO, mMockNativeAdObject)
        Assert.assertTrue(AdCacheManager.hasCache(Definition.AdUnit.INFO))
    }

    @Test
    fun testPutAndGetCache() {
        AdCacheManager.clearCache()
        AdCacheManager.putCacheAd(Definition.AdUnit.INFO, mMockNativeAdObject)
        val cacheNativeAd = AdCacheManager.getCacheAd(Definition.AdUnit.INFO)
        Assert.assertNotNull(cacheNativeAd)
        Assert.assertTrue(cacheNativeAd is NativeAdObject)

        AdCacheManager.clearCache()
        AdCacheManager.putCacheAd(Definition.AdUnit.INFO, mMockBannerAdObject)
        val cacheBannerAd = AdCacheManager.getCacheAd(Definition.AdUnit.INFO)
        Assert.assertNotNull(cacheBannerAd)
        Assert.assertTrue(cacheBannerAd is BannerAdObject)
    }

    @Test
    fun testClearCache() {
        AdCacheManager.clearCache()
        Assert.assertFalse(AdCacheManager.hasCache(Definition.AdUnit.INFO))
    }
}