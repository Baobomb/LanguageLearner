package com.gogolook.adsdk

import android.content.Context
import tw.bao.adsdk.adobject.BannerAdObject
import tw.bao.adsdk.adobject.BaseAdObject
import tw.bao.adsdk.adobject.NativeAdObject
import tw.bao.adsdk.cache.AdCacheManager
import tw.bao.adsdk.fetcher.BannerAdFetcher
import tw.bao.adsdk.fetcher.BaseAdFetcher
import tw.bao.adsdk.fetcher.NativeAdFetcher
import tw.bao.adsdk.listener.AdRequestStatusListener
import gogolook.callgogolook2.mock.BaseTestCase
import junit.framework.Assert
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import tw.bao.adsdk.Definition
import tw.bao.adsdk.AdManager


/**
 * Created by chenweilun on 2017/10/26.
 */
@PrepareForTest(AdManager::class,
        BaseAdFetcher::class,
        PapilioAdObject::class,
        BannerAdFetcher::class,
        NativeAdFetcher::class,
        PapilioAdFetcher::class,
        BaseAdObject::class,
        NativeAdObject::class,
        BannerAdObject::class,
        PapilioAdObject::class)
@RunWith(PowerMockRunner::class)
class WcAdManagerTest : BaseTestCase() {

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
    }

    @After
    @Throws(Exception::class)
    override fun tearDown() {
        super.tearDown()
    }

    @Test
    @Throws(Exception::class)
    fun testRequestFail() {
        var isRequestEnd = false
        initMockRequestFailureAdUnit()
        AdManager.getInstance(Definition.AdUnit.INFO)
                .setAdSourceNeedRequest(Definition.AdSource.PAPILIO, true)
                .setAdSourceNeedRequest(Definition.AdSource.NATIVE, true)
                .setAdSourceNeedRequest(Definition.AdSource.BANNER, true)
                .setRequestStatusListener(object : AdRequestStatusListener {
                    override fun onRequestStart(adUnit: Definition.AdUnit) {

                    }

                    override fun onRequestEnd(adUnit: Definition.AdUnit) {
                        isRequestEnd = true
                    }
                })
                .setIsUsingDebugAdUnit(true)
                .startRequest(mMockContext)

        if (isRequestEnd) {
            val adObject = AdCacheManager.getCacheAd(Definition.AdUnit.INFO)
            Assert.assertNull(adObject)
            AdManager.getInstance(Definition.AdUnit.INFO).stopRequest()
            Assert.assertFalse(AdManager.getInstance(Definition.AdUnit.INFO).isAdRequesting)
        } else {
            throw Exception("AdManager Request flow invalid")
        }
    }

    @Test
    @Throws(Exception::class)
    fun testFetchNativeSuccess() {
        var isRequestEnd = false
        initMockRequestSuccessAdUnit(Definition.AdSource.NATIVE)
        AdManager.getInstance(Definition.AdUnit.INFO)
                .setAdSourceNeedRequest(Definition.AdSource.PAPILIO, false)
                .setAdSourceNeedRequest(Definition.AdSource.NATIVE, true)
                .setAdSourceNeedRequest(Definition.AdSource.BANNER, false)
                .setRequestStatusListener(object : AdRequestStatusListener {
                    override fun onRequestStart(adUnit: Definition.AdUnit) {

                    }

                    override fun onRequestEnd(adUnit: Definition.AdUnit) {
                        isRequestEnd = true
                    }
                })
                .setIsUsingDebugAdUnit(true)
                .startRequest(mMockContext)
        if (isRequestEnd) {
            val adObject = AdCacheManager.getCacheAd(Definition.AdUnit.INFO)
            Assert.assertNotNull(adObject)
            assertThat(adObject, instanceOf(NativeAdObject::class.java))
            AdManager.getInstance(Definition.AdUnit.INFO).stopRequest()
            Assert.assertFalse(AdManager.getInstance(Definition.AdUnit.INFO).isAdRequesting)
        } else {
            throw Exception("AdManager Request flow invalid")
        }
    }

    @Test
    @Throws(Exception::class)
    fun testFetchPapilioSuccess() {
        var isRequestEnd = false
        initMockRequestSuccessAdUnit(Definition.AdSource.PAPILIO)
        AdManager.getInstance(Definition.AdUnit.INFO)
                .setAdSourceNeedRequest(Definition.AdSource.PAPILIO, true)
                .setAdSourceNeedRequest(Definition.AdSource.NATIVE, false)
                .setAdSourceNeedRequest(Definition.AdSource.BANNER, false)
                .setRequestStatusListener(object : AdRequestStatusListener {
                    override fun onRequestStart(adUnit: Definition.AdUnit) {

                    }

                    override fun onRequestEnd(adUnit: Definition.AdUnit) {
                        isRequestEnd = true
                    }
                })
                .setIsUsingDebugAdUnit(true)
                .startRequest(mMockContext)
        if (isRequestEnd) {
            val adObject = AdCacheManager.getCacheAd(Definition.AdUnit.INFO)
            Assert.assertNotNull(adObject)
            assertThat(adObject, instanceOf(PapilioAdObject::class.java))
            AdManager.getInstance(Definition.AdUnit.INFO).stopRequest()
            Assert.assertFalse(AdManager.getInstance(Definition.AdUnit.INFO).isAdRequesting)
        } else {
            throw Exception("AdManager Request flow invalid")
        }
    }

    @Test
    @Throws(Exception::class)
    fun testFetchBannerSuccess() {
        var isRequestEnd = false
        initMockRequestSuccessAdUnit(Definition.AdSource.BANNER)
        AdManager.getInstance(Definition.AdUnit.INFO)
                .setAdSourceNeedRequest(Definition.AdSource.PAPILIO, false)
                .setAdSourceNeedRequest(Definition.AdSource.NATIVE, false)
                .setAdSourceNeedRequest(Definition.AdSource.BANNER, true)
                .setRequestStatusListener(object : AdRequestStatusListener {
                    override fun onRequestStart(adUnit: Definition.AdUnit) {

                    }

                    override fun onRequestEnd(adUnit: Definition.AdUnit) {
                        isRequestEnd = true
                    }
                })
                .setIsUsingDebugAdUnit(true)
                .startRequest(mMockContext)
        if (isRequestEnd) {
            val adObject = AdCacheManager.getCacheAd(Definition.AdUnit.INFO)
            Assert.assertNotNull(adObject)
            assertThat(adObject, instanceOf(BannerAdObject::class.java))
            AdManager.getInstance(Definition.AdUnit.INFO).stopRequest()
            Assert.assertFalse(AdManager.getInstance(Definition.AdUnit.INFO).isAdRequesting)
        } else {
            throw Exception("AdManager Request flow invalid")
        }
    }

    private fun initMockRequestFailureAdUnit() {
        PowerMockito.mockStatic(Context::class.java)
        PowerMockito.spy(AdManager::class.java)
        PowerMockito.spy(PapilioAdFetcher(mMockContext, Definition.AdUnit.INFO)).apply {
            PowerMockito.whenNew(PapilioAdFetcher::class.java).withAnyArguments().thenReturn(this)
            PowerMockito.doNothing().`when`(this).startFetch()
            PowerMockito.`when`(this.startFetch()).then {
                notifyAdFetchFail(Definition.AdSource.PAPILIO, "Error test")
            }
        }
        PowerMockito.spy(NativeAdFetcher(mMockContext, Definition.AdUnit.INFO)).apply {
            PowerMockito.whenNew(NativeAdFetcher::class.java).withAnyArguments().thenReturn(this)
            PowerMockito.doNothing().`when`(this).startFetch()
            PowerMockito.`when`(startFetch()).then {
                notifyAdFetchFail(Definition.AdSource.NATIVE, "Error test")
            }
        }
        PowerMockito.spy(BannerAdFetcher(mMockContext, Definition.AdUnit.INFO)).apply {
            PowerMockito.whenNew(BannerAdFetcher::class.java).withAnyArguments().thenReturn(this)
            PowerMockito.doNothing().`when`(this).startFetch()
            PowerMockito.`when`(startFetch()).then {
                notifyAdFetchFail(Definition.AdSource.BANNER, "Error test")
            }
        }
    }

    private fun initMockRequestSuccessAdUnit(adSourceNeedToFetch: Definition.AdSource) {
        PowerMockito.mockStatic(Context::class.java)
        PowerMockito.spy(AdManager::class.java)
        when (adSourceNeedToFetch) {
            Definition.AdSource.PAPILIO -> mockPapilioFetchSuccess()
            Definition.AdSource.NATIVE -> mockNativeFetchSuccess()
            Definition.AdSource.BANNER -> mockBannerFetchSuccess()
        }
    }

    private fun mockPapilioFetchSuccess() {
        val mockPapilioAdObject = PowerMockito.mock(PapilioAdObject::class.java)
        PowerMockito.spy(PapilioAdFetcher(mMockContext, Definition.AdUnit.INFO)).apply {
            PowerMockito.whenNew(PapilioAdFetcher::class.java).withAnyArguments().thenReturn(this)
            PowerMockito.doNothing().`when`(this).startFetch()
            PowerMockito.`when`(this.startFetch()).then {
                notifyAdFetchSuccess(Definition.AdSource.NATIVE, mockPapilioAdObject)
            }
        }
    }

    private fun mockNativeFetchSuccess() {
        val mockNativeAdObject = PowerMockito.mock(NativeAdObject::class.java)
        PowerMockito.spy(NativeAdFetcher(mMockContext, Definition.AdUnit.INFO)).apply {
            PowerMockito.whenNew(NativeAdFetcher::class.java).withAnyArguments().thenReturn(this)
            PowerMockito.doNothing().`when`(this).startFetch()
            PowerMockito.`when`(this.startFetch()).then {
                notifyAdFetchSuccess(Definition.AdSource.NATIVE, mockNativeAdObject)
            }
        }
    }

    private fun mockBannerFetchSuccess() {
        val mockBannerAdObject = PowerMockito.mock(BannerAdObject::class.java)
        PowerMockito.spy(BannerAdFetcher(mMockContext, Definition.AdUnit.INFO)).apply {
            PowerMockito.whenNew(BannerAdFetcher::class.java).withAnyArguments().thenReturn(this)
            PowerMockito.doNothing().`when`(this).startFetch()
            PowerMockito.`when`(this.startFetch()).then {
                notifyAdFetchSuccess(Definition.AdSource.NATIVE, mockBannerAdObject)
            }
        }
    }

}
