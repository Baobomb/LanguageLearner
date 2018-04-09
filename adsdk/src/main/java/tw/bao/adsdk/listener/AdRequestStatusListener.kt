package tw.bao.adsdk.listener

import tw.bao.adsdk.Definition

/**
 * Created by chenweilun on 2017/10/13.
 */

interface AdRequestStatusListener {

    fun onRequestStart(adUnit: Definition.AdUnit)

    fun onRequestEnd(adUnit: Definition.AdUnit)
}
