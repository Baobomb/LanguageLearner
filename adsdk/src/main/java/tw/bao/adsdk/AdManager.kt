package tw.bao.adsdk

import android.content.Context
import android.util.SparseArray
import tw.bao.adsdk.Definition.AdSource.*
import tw.bao.adsdk.Definition.FetchState.*
import tw.bao.adsdk.Definition.RequestState.*
import tw.bao.adsdk.adobject.BaseAdObject
import tw.bao.adsdk.cache.AdCacheManager
import tw.bao.adsdk.debug.AdLog
import tw.bao.adsdk.error.AdErrorCode
import tw.bao.adsdk.fetcher.BannerAdFetcher
import tw.bao.adsdk.fetcher.BaseAdFetcher
import tw.bao.adsdk.fetcher.NativeAdFetcher
import tw.bao.adsdk.listener.AdFetchStatusListener
import tw.bao.adsdk.listener.AdRequestStatusListener
import tw.bao.adsdk.utils.AdUtils

/**
 * Created by chenweilun on 2017/10/16.
 */

class AdManager private constructor(private val mAdUnit: Definition.AdUnit) {

    private val mAdSourceNeedRequestParamList: MutableSet<Definition.AdSource> by lazy { HashSet<Definition.AdSource>() }
    private var mAdFetcher: BaseAdFetcher? = null
    private var mAdRequestStatusListener: AdRequestStatusListener? = null
    private var mContext: Context? = null
    private var mIsUsingDebugAdUnit = false

    var isAdRequesting = false
        private set
    var fetchErrorMessage: String? = null
        private set


    private val mAdFetchStatusListener = object : AdFetchStatusListener {
        override fun onFetchStart(adSource: Definition.AdSource) {
            AdLog.D(LOG_TAG, mAdUnit, mIsUsingDebugAdUnit, adSource, FETCH_START)
        }

        override fun onFetchSkip(adSource: Definition.AdSource) {
            AdLog.D(LOG_TAG, mAdUnit, mIsUsingDebugAdUnit, adSource, FETCH_SKIP)
            mAdFetcher?.removeAdFetchStatusListener()
            mAdFetcher = null
            requestNext(adSource)
        }

        override fun onFetchFailure(adSource: Definition.AdSource, errorMessage: String?) {
            AdLog.D(LOG_TAG, mAdUnit, mIsUsingDebugAdUnit, adSource, FETCH_FAIL, errorMessage)
            fetchErrorMessage = errorMessage
            mAdFetcher?.removeAdFetchStatusListener()
            mAdFetcher = null
            requestNext(adSource)
        }

        override fun onFetchSuccess(adSource: Definition.AdSource, adObject: BaseAdObject?) {
            AdLog.D(LOG_TAG, mAdUnit, mIsUsingDebugAdUnit, adSource, FETCH_SUCCESS)
            if (adSource != BANNER || mContext != null) {
                AdCacheManager.putCacheAd(mAdUnit, adObject)
            }
            mAdFetcher?.removeAdFetchStatusListener()
            mAdFetcher = null
            notifyAdRequestStatus(REQUEST_END)
        }
    }

    fun setRequestStatusListener(adRequestStatusListener: AdRequestStatusListener?): AdManager {
        this.mAdRequestStatusListener = adRequestStatusListener
        return this
    }

    fun setIsUsingDebugAdUnit(isUsingDebugAdUnit: Boolean): AdManager {
        this.mIsUsingDebugAdUnit = isUsingDebugAdUnit
        return this
    }

    fun startRequest(context: Context?) {
        this.mContext = context
        if (isAdRequesting) {
            return
        }
        if (!AdUtils.isNeedShowAd) {
            fetchErrorMessage = AdErrorCode.CLIENT_ERROR_MESSAGE.ERROR_NO_NEED_REQUEST.message
            stopRequest()
            return
        }
        notifyAdRequestStatus(REQUEST_START)
        requestNext(null)
    }

    fun stopRequest() {
        mContext = null
        mAdRequestStatusListener = null
        mAdFetcher?.stopFetch()
        notifyAdRequestStatus(REQUEST_STOP)
    }

    fun setAdSourceNeedRequest(adSource: Definition.AdSource, isNeedRequest: Boolean): AdManager {
        if (isNeedRequest) {
            mAdSourceNeedRequestParamList.add(adSource)
        } else if (mAdSourceNeedRequestParamList.contains(adSource)) {
            mAdSourceNeedRequestParamList.remove(adSource)
        }
        return this
    }

    private fun getIfAdSourceNeedRequest(adSource: Definition.AdSource): Boolean {
        return mAdSourceNeedRequestParamList.contains(adSource)
    }

    private fun requestNext(currentRequestAdSource: Definition.AdSource?) {
        when (currentRequestAdSource) {
            NATIVE -> requestSource(BANNER)
            BANNER -> notifyAdRequestStatus(REQUEST_END)
            else -> requestSource(NATIVE)
        }
    }

    private fun requestSource(adSource: Definition.AdSource) {
        mAdFetcher = when (adSource) {
            NATIVE -> NativeAdFetcher(mContext, mAdUnit)
            BANNER -> BannerAdFetcher(mContext, mAdUnit)
        }
        mAdFetcher?.apply {
            addAdFetchStatusListener(mAdFetchStatusListener)
            setIsUsingDebugAdUnit(this@AdManager.mIsUsingDebugAdUnit)
            if (!getIfAdSourceNeedRequest(adSource)) {
                notifyAdFetchSkip(adSource)
                return
            }
            notifyAdFetchStart(adSource)
            startFetch()
            return
        }
        notifyAdRequestStatus(REQUEST_END)
    }

    private fun notifyAdRequestStatus(requestState: Definition.RequestState) {
        AdLog.D(LOG_TAG, mAdUnit, mIsUsingDebugAdUnit, requestState)
        isAdRequesting = requestState == REQUEST_START
        mAdRequestStatusListener?.apply {
            when (requestState) {
                REQUEST_START -> onRequestStart(mAdUnit)
                REQUEST_END -> onRequestEnd(mAdUnit)
                REQUEST_STOP -> {
                }
            }
        }
    }

    companion object {
        private val LOG_TAG = AdManager::class.java.simpleName

        @Volatile private var sInstancesMap = SparseArray<AdManager>()

        @JvmStatic
        fun getInstance(adUnit: Definition.AdUnit): AdManager {
            synchronized(sInstancesMap) {
                val index = adUnit.ordinal
                var manager: AdManager? = sInstancesMap.get(index)
                if (manager == null) {
                    manager = AdManager(adUnit)
                    sInstancesMap.put(index, manager)
                }
                return manager
            }
        }
    }


}
