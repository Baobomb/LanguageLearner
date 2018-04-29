package tw.bao.languagelearner.model

import java.util.*

/**
 * Created by bao on 2017/11/8.
 */
data class WordData(var chineseWord: String, var romanText: String, var engWord: String, var japWord: String, var krWord: String) {
    fun getWord(): String {
        val language = Locale.getDefault().language
        return when (language) {
            Locale.ENGLISH.language -> engWord
            Locale.JAPAN.language -> japWord
            Locale.JAPANESE.language -> japWord
            Locale.KOREA.language -> krWord
            Locale.KOREAN.language -> krWord
            else -> engWord
        }
    }
}