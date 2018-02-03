package com.gogolook.adsdk.utils

import gogolook.callgogolook2.mock.BaseTestCase
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import tw.bao.adsdk.utils.UtilsAdRenderer

/**
 * Created by chenweilun on 2017/10/27.
 */

@PrepareForTest(
        UtilsAdRenderer::class
)
@RunWith(PowerMockRunner::class)
class UtilsAdRendererTest : BaseTestCase() {

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
    fun testGetDefaultAdIconRandomly() {
        Assert.assertTrue(UtilsAdRenderer.defaultAdIconRandomly > 0)
    }
}