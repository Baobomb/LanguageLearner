package tw.bao.languagelearner.info.utils

import tw.bao.languagelearner.utils.Prefs

/**
 * Created by bao on 2017/12/14.
 */
object UtilsInfo {


    public fun getUserTotalAnswerScore(): Int {
        return Prefs.getInt(Prefs.KEY_SCORE, 0)
    }

    public fun getUserCurrentScore(): Int {
        return getUserTotalAnswerScore() % 100
    }

    public fun getUserLevel(): Int {
        return getUserTotalAnswerScore() / 100
    }

    public fun upUserScore() {
        var currentTotalScore = getUserTotalAnswerScore()
        currentTotalScore += 10
        Prefs.putInt(Prefs.KEY_SCORE, currentTotalScore)
    }
}