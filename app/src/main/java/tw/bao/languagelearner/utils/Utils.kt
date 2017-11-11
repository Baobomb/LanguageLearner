package tw.bao.languagelearner.utils

import java.util.*

/**
 * Created by bao on 2017/11/11.
 */
object Utils {

    fun getRamdonInts(from: Int, to: Int): Array<Int> {
        val list = ArrayList<Int>()
        list += from..to
        Collections.shuffle(list)
        return arrayOf(list[0], list[1], list[2], list[3])
    }
}