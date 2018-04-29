package tw.bao.languagelearner.info.utils

import tw.bao.languagelearner.utils.Prefs
import java.util.*

/**
 * Created by bao on 2017/12/14.
 */
object UtilsInfo {

    private val DEBUG_LEVEL = true

    public fun getUserTotalAnswerScore(): Int = Prefs.getInt(Prefs.KEY_SCORE, 0)

    public fun getUserCurrentScore(): Int = getUserTotalAnswerScore() % 100

    public fun getUserLevel(): Int = if (DEBUG_LEVEL) 1000 else getUserTotalAnswerScore() / 100

    public fun getUserAnswerCorrectNums(): Int = Prefs.getInt(Prefs.KEY_ANSWER_CORRECT_NUMS, 0)

    public fun getUserAnswerTotalNums(): Int = Prefs.getInt(Prefs.KEY_TOTAL_ANSWER_NUMS, 0)

    public fun getUserAnswerCorrectRate(): Float {
        if (getUserAnswerTotalNums() == 0) {
            return 0f
        }
        return getUserAnswerCorrectNums().toFloat() / getUserAnswerTotalNums().toFloat()
    }

    public fun upUserScore() {
        var currentTotalScore = getUserTotalAnswerScore()
        currentTotalScore += 10
        Prefs.putInt(Prefs.KEY_SCORE, currentTotalScore)
    }

    public fun setUserAnswerResult(isCorrect: Boolean) {
        val totalAnswer = Prefs.getInt(Prefs.KEY_TOTAL_ANSWER_NUMS, 0)
        Prefs.putInt(Prefs.KEY_ANSWER_CORRECT_NUMS, (totalAnswer + 1))
        if (isCorrect) {
            val totalCorrect = Prefs.getInt(Prefs.KEY_ANSWER_CORRECT_NUMS, 0)
            Prefs.putInt(Prefs.KEY_ANSWER_CORRECT_NUMS, (totalCorrect + 1))
        }
    }

    public fun getDefaultLanguageLocale(): Locale {
        val language = Locale.getDefault().language
        return when (language) {
            Locale.ENGLISH.language -> Locale.ENGLISH
            Locale.JAPAN.language -> Locale.JAPANESE
            Locale.JAPANESE.language -> Locale.JAPANESE
            Locale.KOREA.language -> Locale.KOREAN
            Locale.KOREAN.language -> Locale.KOREAN
            else -> Locale.ENGLISH
        }
    }

}