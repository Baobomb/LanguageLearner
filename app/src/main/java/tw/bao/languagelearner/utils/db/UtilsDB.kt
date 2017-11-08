package tw.bao.languagelearner.utils.db

import android.text.TextUtils
import android.util.Log
import java.util.*

/**
 * Created by bao on 2017/11/8.
 */
object UtilsDB {


    fun parseSQLCreateCommand(condition: String): String {
        val commands = ArrayList<Any>()
        val columns = condition.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        for (column in columns) {
            val commandArray = getSingleCommands(column)
            val tableCommand = StringBuilder()
            var columnName: String? = null
            var columnType: String? = null
            var columnPrimaryKey: String? = null
            var columnNullable: String? = null
            var columnDefault: String? = null
            var columnAutoIncrement: String? = null
            columnName = commandArray[0] + " "
            columnType = parseColumnType(commandArray[1]) + " "
            columnPrimaryKey = if (columnType.contains("INTEGER") && java.lang.Boolean.parseBoolean(commandArray[4])) "PRIMARY KEY " else ""
            columnAutoIncrement = if (columnType.contains("INTEGER") && java.lang.Boolean.parseBoolean(commandArray[4])) "AUTOINCREMENT " else ""
            columnDefault = if (!columnType.contains("INTEGER") && !TextUtils.isEmpty(commandArray[3])) "DEFAULT'" + commandArray[3] + "' " else ""
            columnNullable = if (!columnType.contains("INTEGER") && TextUtils.isEmpty(columnDefault) && !java.lang.Boolean.parseBoolean(commandArray[2])) "NOT NULL " else ""
            tableCommand.append(columnName)
                    .append(columnType)
                    .append(columnPrimaryKey)
                    .append(columnAutoIncrement)
                    .append(columnNullable)
                    .append(columnDefault)
            commands.add(tableCommand.toString())
        }
        val finalCommand = StringBuilder()
        for (i in commands.indices) {
            finalCommand.append(commands.get(i))
            if (i < commands.size - 1) {
                finalCommand.append(",")
            }
        }
        Log.d("SQLCommand", finalCommand.toString())
        return finalCommand.toString()
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