package com.gogolook.adsdk.config

import tw.bao.adsdk.Definition
import gogolook.callgogolook2.mock.BaseTestCase
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import tw.bao.adsdk.config.MopubConfigHelper

/**
 * Created by chenweilun on 2017/12/4.
 */

@PrepareForTest(
        MopubConfigHelper::class

)
@RunWith(PowerMockRunner::class)
class MopubConfigHelperTest : BaseTestCase() {

    val DEV_MOPUB_BLOCK_STICKY_AD_UNIT_ID = "6888417615164c3cbb3e7bcabe008347"
    val DEV_MOPUB_CHARGING_SCREEN_AD = "e322ac3753714eab918efd62f34bebfd"
    val MOPUB_BLOCK_STICKY_AD_UNIT_ID = "e674985a2430498f9d18b089e790b8a3"
    val MOPUB_CHARGING_SCREEN_AD = "9bfaa4b1cd344149aae8ef3ba02bf9ef"

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
    }

    @Before
    @Throws(Exception::class)
    override fun tearDown() {
        super.tearDown()
    }

    @Test
    fun test() {
        Assert.assertSame(MopubConfigHelper.getUnitId(Definition.AdUnit.INFO, true), DEV_MOPUB_BLOCK_STICKY_AD_UNIT_ID)
        Assert.assertSame(MopubConfigHelper.getUnitId(Definition.AdUnit.CHARGING_SCREEN, true), DEV_MOPUB_CHARGING_SCREEN_AD)
        Assert.assertSame(MopubConfigHelper.getUnitId(Definition.AdUnit.INFO, false), MOPUB_BLOCK_STICKY_AD_UNIT_ID)
        Assert.assertSame(MopubConfigHelper.getUnitId(Definition.AdUnit.CHARGING_SCREEN, false), MOPUB_CHARGING_SCREEN_AD)
        Assert.assertNotNull(MopubConfigHelper.getRenderer(Definition.AdUnit.INFO))
        Assert.assertNotNull(MopubConfigHelper.getRenderer(Definition.AdUnit.CHARGING_SCREEN))
        Assert.assertNotNull(MopubConfigHelper.getParameters(Definition.AdUnit.INFO))
        Assert.assertNotNull(MopubConfigHelper.getParameters(Definition.AdUnit.CHARGING_SCREEN))

    }
}