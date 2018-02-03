package tw.bao.adsdk.fetcher

import android.content.Context
import android.support.annotation.CallSuper
import tw.bao.adsdk.Definition
import tw.bao.adsdk.adobject.BaseAdObject
import tw.bao.adsdk.listener.AdFetchStatusListener

/**
 * Created by chenweilun on 2017/10/17.
 */

abstract class BaseAdFetcher(internal var context: Context?, internal var adUnit: Definition.AdUnit) {
    private var mAdFetchStatusListener: AdFetchStatusListener? = null
    var mIsUsingDebugAdUnit = false

    fun setIsUsingDebugAdUnit(isUsingDebugAdUnit: Boolean) {
        this.mIsUsingDebugAdUnit = isUsingDebugAdUnit
    }

    abstract fun startFetch()

    @CallSuper
    open fun stopFetch() {
        context = null
    }

    fun addAdFetchStatusListener(adFetchStatusListener: AdFetchStatusListener) {
        this.mAdFetchStatusListener = adFetchStatusListener
    }

    fun removeAdFetchStatusListener() {
        this.mAdFetchStatusListener = null
    }

    fun notifyAdFetchStart(adSource: Definition.AdSource) {
        mAdFetchStatusListener?.onFetchStart(adSource)
    }

    fun notifyAdFetchSkip(adSource: Definition.AdSource) {
        mAdFetchStatusListener?.onFetchSkip(adSource)
    }

    fun notifyAdFetchFail(adSource: Definition.AdSource, errorMessage: String) {
        mAdFetchStatusListener?.onFetchFailure(adSource, errorMessage)
    }


    fun notifyAdFetchSuccess(adSource: Definition.AdSource, adObject: BaseAdObject) {
        mAdFetchStatusListener?.onFetchSuccess(adSource, adObject)
    }

    companion object {
        internal val LOGTAG = BaseAdFetcher::class.java.simpleName
    }
}
