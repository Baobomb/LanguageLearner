package tw.bao.adsdk.debug

import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.util.Log
import tw.bao.adsdk.Definition
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by chenweilun on 2017/10/18.
 */

object AdLog {
    private val MAX_SIZE: Int by lazy { 80 }
    private val sSb: SpannableStringBuilder  by lazy { SpannableStringBuilder() }
    private val AD_LOG_TAG: String by lazy { "[AD_Manager]" }
    private val sLogArrayMap: HashMap<Definition.AdUnit, ArrayList<Array<String>>> by lazy { HashMap<Definition.AdUnit, ArrayList<Array<String>>>() }

    @JvmField
    var sIsDebug = true

    private val time: String
        get() {
            val formatter = SimpleDateFormat("HH:mm:ss")
            return formatter.format(Date())
        }

    /**
     * Should init before ad manager start log every time
     */
    @JvmStatic
    fun initAdLogByUnit(adUnit: Definition.AdUnit) {
        sLogArrayMap[adUnit]?.clear()
    }

    @JvmStatic
    fun D(tag: String?, adUnit: Definition.AdUnit,
          isDebugAdUnit: Boolean, requestState: Definition.RequestState,
          result: String? = null) {
        if (sIsDebug || AdLogViewer.isActive) {
            val adUnitState = if (isDebugAdUnit) "DEBUG" else "PRODUCT"
            D(tag, adUnit, " [AdUnit] : ${adUnit.msg}($adUnitState) [Status] : ${requestState.msg} [Result] : $result")
        }
    }

    @JvmStatic
    fun D(tag: String, adUnit: Definition.AdUnit,
          isDebugAdUnit: Boolean, adSource: Definition.AdSource,
          fetchState: Definition.FetchState,
          message: String? = null) {
        if (sIsDebug || AdLogViewer.isActive) {
            val adUnitState = if (isDebugAdUnit) "DEBUG" else "PRODUCT"
            D(tag, adUnit, " [AdUnit] : ${adUnit.msg}($adUnitState) [AdSource] : ${adSource.msg} [Status] : ${fetchState.msg} [Message] : $message")
        }
    }

    private fun D(tag: String?, adUnit: Definition.AdUnit, log: String) {
        val arrayList: ArrayList<Array<String>>? =
                if (!sLogArrayMap.containsKey(adUnit)) {
                    ArrayList()
                } else {
                    sLogArrayMap[adUnit]
                }
        arrayList?.takeIf { it.size > MAX_SIZE }?.removeAt(0)
        Log.d(AD_LOG_TAG, (if (TextUtils.isEmpty(tag)) "" else tag) + " : " + log)
        val logMessage = arrayOf(time, log)
        arrayList?.apply {
            add(logMessage)
            sLogArrayMap.put(adUnit, this)
            AdLogViewer.refreshDisplay(adUnit)
        }
    }

    fun getAdLog(adUnit: Definition.AdUnit): SpannableStringBuilder {

        val arrayList: ArrayList<Array<String>>?
        if (!sLogArrayMap.containsKey(adUnit)) {
            return sSb
        } else {
            arrayList = sLogArrayMap[adUnit]
        }
        arrayList!!.forEach {
            sSb.append(it[0])
            sSb.append(it[1])
            sSb.append("\n")
            sSb.append("-------")
            sSb.append("\n")
        }
        return sSb
    }
}
