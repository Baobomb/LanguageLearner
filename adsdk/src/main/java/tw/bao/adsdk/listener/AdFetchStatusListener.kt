package tw.bao.adsdk.listener

import tw.bao.adsdk.Definition
import tw.bao.adsdk.adobject.BaseAdObject

/**
 * Created by chenweilun on 2017/10/13.
 */

interface AdFetchStatusListener {

    fun onFetchStart(adSource: Definition.AdSource)

    fun onFetchSkip(adSource: Definition.AdSource)

    fun onFetchFailure(adSource: Definition.AdSource, errorMessage: String?)

    fun onFetchSuccess(adSource: Definition.AdSource, adObject: BaseAdObject?)
}
