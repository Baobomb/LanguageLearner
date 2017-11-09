package tw.bao.languagelearner.utils.db

import tw.bao.languagelearner.model.WordData
import tw.bao.languagelearner.model.WordDatas
import java.util.*

/**
 * Created by bao on 2017/11/8.
 */
object UtilsDB {


    fun parseSQLCreateCommand(condition: String): WordDatas {
        val wordDataArray = condition.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val wordDataList = wordDataArray
                .map { getSingleCommands(it) }
                .map { WordData(it[1], it[2], it[0]) }
        return WordDatas("", wordDataList)
    }

    private fun getSingleCommands(command: String): List<String> {
        val commands = command.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return Arrays.asList(*commands)
    }

    fun parseColumnType(command: String): String {
        var lowerCaseType = command.toLowerCase()
        lowerCaseType = lowerCaseType.replace("integer", "INTEGER")
        lowerCaseType = lowerCaseType.replace("varchar", "VARCHAR")
        lowerCaseType = lowerCaseType.replace("text", "TEXT")
        return if (lowerCaseType.contains("INTEGER")) "INTEGER" else lowerCaseType
    }
}