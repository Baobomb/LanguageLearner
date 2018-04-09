package tw.bao.adsdk.error

/**
 * Created by Bao on 2017/10/23.
 * Define ad error code here
 */

//TODO Use for event tracker and AdLog
object AdErrorCode {

    private val CLIENT_ERROR_PREFEX: String by lazy { "Client error " }

    enum class CLIENT_ERROR_MESSAGE(public val message: String) {
        ERROR_UNKNOWN(CLIENT_ERROR_PREFEX + "unknown"),
        ERROR_AD_OBJECT_INVALID(CLIENT_ERROR_PREFEX + "ad object invalid"),
        ERROR_CONTEXT_INVALID(CLIENT_ERROR_PREFEX + "context invalid"),
        ERROR_AD_UNIT_ID_INVALID(CLIENT_ERROR_PREFEX + "ad unit id invalid"),
        ERROR_NO_NEED_REQUEST(CLIENT_ERROR_PREFEX + " no need to request ad"),
        ERROR_NETWORK_NOT_ENABLE(CLIENT_ERROR_PREFEX + " network not enable");
    }
}
