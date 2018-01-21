package tw.bao.languagelearner.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.*

/**
 * Created by bao on 2018/1/21.
 */
object UtilsTTS {

    private var mTts: TextToSpeech? = null
    private var mStatus: Int = TextToSpeech.ERROR

    fun initTTSService(context: Context): Boolean {
        if (mTts == null) {
            mTts = TextToSpeech(context, TextToSpeech.OnInitListener {
                mStatus = it
            })
        }
        mTts?.takeIf { mStatus == TextToSpeech.SUCCESS }?.apply {
            this.setSpeechRate(0.5f)
            return true
        }
        return false
    }

    fun destroy() {
        mTts?.takeIf { it.isSpeaking }?.stop()
        mTts?.shutdown()
    }

    fun speech(textToSpeech: String, locale: Locale): Boolean {
        mTts?.apply {
            if (this.isLanguageAvailable(locale) == TextToSpeech.LANG_AVAILABLE) {
                this.language = locale
                mTts?.speak(textToSpeech, TextToSpeech.QUEUE_FLUSH, null)
                return true
            }
        }
        return false
    }

}