package tw.bao.adsdk.cache

import tw.bao.adsdk.Definition
import tw.bao.adsdk.adobject.BaseAdObject

import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Bao on 10/13/17.
 */

object AdCacheManager {

    private val mAdCacheHashMap: ConcurrentHashMap<Definition.AdUnit, BaseAdObject> by lazy {
        ConcurrentHashMap<Definition.AdUnit, BaseAdObject>()
    }

    @JvmStatic
    fun hasCache(adUnit: Definition.AdUnit): Boolean {
        mAdCacheHashMap[adUnit]?.takeIf { it.isExpired }?.also { removeCacheAd(adUnit) }
        return mAdCacheHashMap.containsKey(adUnit)
    }

    @JvmStatic
    fun getCacheAd(adUnit: Definition.AdUnit): BaseAdObject? {
        return mAdCacheHashMap[adUnit]?.also { removeCacheAd(adUnit) }
    }

    @JvmStatic
    fun putCacheAd(adUnit: Definition.AdUnit, cacheAd: BaseAdObject?) {
        cacheAd?.apply { mAdCacheHashMap.put(adUnit, this) }
    }

    private fun removeCacheAd(adUnit: Definition.AdUnit) {
        mAdCacheHashMap.remove(adUnit)
    }

    @JvmStatic
    fun clearCache() {
        mAdCacheHashMap.clear()
    }
}