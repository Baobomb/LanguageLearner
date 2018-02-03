package tw.bao.adsdk

/**
 * Created by chenweilun on 2017/10/13.
 */

object Definition {

    @JvmField
    val BLOCK_AD_AB_TEST_CLOSE = 0

    @JvmField
    val BLOCK_AD_AB_TEST_ONLY_BANNER = 1

    @JvmField
    val BLOCK_AD_AB_TEST_ONLY_NATIVE = 2

    enum class AdUnit(public val msg: String) {
        INFO("Info")
    }

    enum class AdSource(public val msg: String) {
        BANNER("Banner"), NATIVE("Native");
    }


    enum class RequestState(public val msg: String) {
        REQUEST_START("Request Start"),
        REQUEST_END("Request End"),
        REQUEST_STOP("Request Stop");
    }

    enum class FetchState(public val msg: String) {
        FETCH_START("Fetch Start"),
        FETCH_SKIP("Fetch Skip"),
        FETCH_SUCCESS("Fetch Success"),
        FETCH_FAIL("Fetch Fail");
    }
}