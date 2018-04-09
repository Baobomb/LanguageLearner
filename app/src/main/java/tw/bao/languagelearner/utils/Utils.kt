package tw.bao.languagelearner.utils

import android.content.Context
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * Created by bao on 2017/11/11.
 */
object Utils {

    fun getRandomFourInts(from: Int, to: Int): Array<Int> {
        ArrayList<Int>().let {
            it += from..to
            Collections.shuffle(it)
            return arrayOf(it[0], it[1], it[2], it[3])
        }
    }

    fun getRandomInts(from: Int, to: Int): ArrayList<Int> {
        ArrayList<Int>().let {
            it += from..to
            Collections.shuffle(it)
            return it
        }
    }

    fun showShowAnswerDialog(): Boolean {
        val isOpenAnswerDialog = Prefs.getBoolean(Prefs.KEY_IS_ANSWER_DIALOG_OPEN, false)
        val isRestrictAnswerDialogOpenTime = Prefs.getBoolean(Prefs.KEY_IS_RESTRICT_ANSWER_DIALOG_SHOW_TIMES, false)
        val restrictAnswerDialogShowTimes = Prefs.getInt(Prefs.KEY_RESTRICT_ANSWER_DIALOG_SHOW_TIMES, -1)
        if (!isOpenAnswerDialog) {
            return false
        }
        if (!isRestrictAnswerDialogOpenTime || restrictAnswerDialogShowTimes <= 0) {
            return true
        }

        val openTimesInHalfDay = Prefs.getInt(Prefs.KEY_ANSWER_DIALOG_OPEN_TIMES_IN_12_HOURS, 0)
        val lastOpenDialogTime = Prefs.getLong(Prefs.KEY_LAST_ANSWER_DIALOG_OPEN_TIME, -1)
        val currentTime = System.currentTimeMillis()
        if ((currentTime - lastOpenDialogTime <= TimeUnit.DAYS.toMillis(1) / 2) && openTimesInHalfDay >= 3) {
            return false
        } else if ((currentTime - lastOpenDialogTime >= TimeUnit.DAYS.toMillis(1) / 2) && openTimesInHalfDay >= 3) {
            Prefs.putInt(Prefs.KEY_ANSWER_DIALOG_OPEN_TIMES_IN_12_HOURS, 0)
        }
        val openTimes = Prefs.getInt(Prefs.KEY_ANSWER_DIALOG_OPEN_TIMES_IN_12_HOURS, 0)

        Prefs.putInt(Prefs.KEY_ANSWER_DIALOG_OPEN_TIMES_IN_12_HOURS, openTimes + 1)
        Prefs.putLong(Prefs.KEY_LAST_ANSWER_DIALOG_OPEN_TIME, currentTime)
        return true
    }

    fun dp2px(context: Context, dpValue: Float): Int = (dpValue * context.applicationContext.resources.displayMetrics.density + 0.5f).toInt()
}