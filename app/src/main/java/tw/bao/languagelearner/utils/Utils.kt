package tw.bao.languagelearner.utils

import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by bao on 2017/11/11.
 */
object Utils {

    fun getRamdonInts(from: Int, to: Int): Array<Int> {
        ArrayList<Int>().let {
            it += from..to
            Collections.shuffle(it)
            return arrayOf(it[0], it[1], it[2], it[3])
        }
    }
}