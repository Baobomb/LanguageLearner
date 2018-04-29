package tw.bao.languagelearner.utils

import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import tw.bao.languagelearner.MyApplication
import java.util.*


/**
 * Created by bao on 2018/1/21.
 */
object UtilsTTS {

    private var mTts: TextToSpeech? = null
    private val GOOGLE_TTS_ENGINE = "com.google.android.tts"
    private var mStatus: Int = TextToSpeech.ERROR

    fun initTTSService(context: Context): Boolean {
        if (mTts == null) {
            mTts = TextToSpeech(context, TextToSpeech.OnInitListener {
                mStatus = it
            }, GOOGLE_TTS_ENGINE)
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
            } else {
                showSetEngineHint()
            }
        }
        return false
    }

    private fun showSetEngineHint() {
        //TODO : show dialog to tell user choose gogole engine forever
        val checkTTSIntent = Intent()
        checkTTSIntent.action = TextToSpeech.Engine.ACTION_CHECK_TTS_DATA
        MyApplication.getGlobalContext()?.startActivity(checkTTSIntent)
    }
}