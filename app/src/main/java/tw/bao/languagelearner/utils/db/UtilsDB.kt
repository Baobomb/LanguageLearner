package tw.bao.languagelearner.utils.db

import android.content.Context
import tw.bao.languagelearner.model.WordData
import tw.bao.languagelearner.model.WordDatas
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*


/**
 * Created by bao on 2017/11/8.
 */
object UtilsDB {
    fun parseWordDatas(type: String, condition: String): WordDatas {
        val wordDataArray = condition.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val wordDataList: List<WordData> = wordDataArray
                .map { getSingleCommands(it) }
                .map { WordData(it[1], it[2], it[0]) }
        return WordDatas(type, wordDataList.toMutableList())
    }

    private fun getSingleCommands(command: String): MutableList<String> {
        val commands = command.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return Arrays.asList(*commands)
    }

    @Throws(IOException::class)
    fun readFromAssets(context: Context, filename: String): String {
        val reader = BufferedReader(InputStreamReader(context.getAssets().open(filename)))
        // do reading, usually loop until end of file reading
        val sb = StringBuilder()
        var mLine = reader.readLine()
        while (mLine != null) {
            sb.append(mLine) // process line
            mLine = reader.readLine()
        }
        reader.close()
        return sb.toString()
    }
}