package tw.bao.languagelearner.info.utils

import tw.bao.languagelearner.utils.Prefs

/**
 * Created by bao on 2017/12/14.
 */
object UtilsInfo {


    public fun getUserTotalAnswerScore(): Int = Prefs.getInt(Prefs.KEY_SCORE, 0)

    public fun getUserCurrentScore(): Int = getUserTotalAnswerScore() % 100

    public fun getUserLevel(): Int = getUserTotalAnswerScore() / 100

    public fun getUserAnswerCorrectNums(): Int = Prefs.getInt(Prefs.KEY_ANSWER_CORRECT_NUMS, 0)

    public fun getUserAnswerTotalNums(): Int = Prefs.getInt(Prefs.KEY_TOTAL_ANSWER_NUMS, 0)

    public fun getUserAnswerCorrectRate(): Float = getUserAnswerCorrectNums().toFloat() / getUserAnswerTotalNums().toFloat()

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

}