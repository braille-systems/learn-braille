package com.github.braillesystems.learnbraille.utils

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import timber.log.Timber
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set
import kotlin.system.exitProcess

// TODO refactor

class SpeechRecognition(
    private val fragment: Fragment,
    private val preferenceRepository: PreferenceRepository
) {

    private var mostRecentUtteranceID: String? = null
    private lateinit var mTTs: TextToSpeech
    private var mSpeechRecognizer: SpeechRecognizer? = null
    private var mSpeechRecognizerIntent: Intent? = null

    init {
        runIf(preferenceRepository.speechRecognitionEnabled) {
            try {
                initialize()
            } catch (e: Throwable) {
                Timber.e(e)
            }
        }
    }

    private fun initialize() = fragment.apply {
        mTTs = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                mTTs.language = Locale.UK
                mostRecentUtteranceID = (Random().nextInt() % 9999999).toString() + ""

                val params: MutableMap<String, String?> = HashMap()
                params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = mostRecentUtteranceID
                @Suppress("DEPRECATION", "UNCHECKED_CAST")
                mTTs.speak(
                    getString(R.string.exit_question), TextToSpeech.QUEUE_FLUSH,
                    params as HashMap<String, String>?
                )
                mTTs.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onDone(utteranceId: String) {
                        if (utteranceId != mostRecentUtteranceID) {
                            return
                        }
                        activity?.runOnUiThread {
                            if (mSpeechRecognizer != null) {
                                mSpeechRecognizer!!.startListening(mSpeechRecognizerIntent)
                            }
                        }
                    }

                    override fun onError(p0: String?) = Unit
                    override fun onStart(p0: String?) = Unit
                })

            }
        })

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        mSpeechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        mSpeechRecognizerIntent?.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        mSpeechRecognizerIntent?.putExtra(
            RecognizerIntent.EXTRA_CALLING_PACKAGE,
            context?.packageName
        )
        mSpeechRecognizer?.setRecognitionListener(SpeechRecognitionListener(fragment))
    }

    fun onDestroy() = runIf(preferenceRepository.speechRecognitionEnabled) {
        try {
            mSpeechRecognizer?.destroy()
        } catch (e: Throwable) {
            Timber.e(e)
        }
    }
}

private class SpeechRecognitionListener(fragment: Fragment) : RecognitionListener {

    override fun onBeginningOfSpeech() = Unit
    override fun onBufferReceived(buffer: ByteArray) = Unit
    override fun onEndOfSpeech() = Unit
    override fun onError(error: Int) = Unit
    override fun onEvent(eventType: Int, params: Bundle) = Unit
    override fun onPartialResults(partialResults: Bundle) = Unit
    override fun onReadyForSpeech(params: Bundle) = Unit
    override fun onRmsChanged(rmsdB: Float) = Unit

    private val hostFragment: Fragment = fragment

    override fun onResults(results: Bundle) {
        try {
            val matches =
                results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?: return
            Timber.i("Speech recognized: $matches")
            if (matches[0] == "да") {
                exitProcess(0)
            }
            if (matches[0] == "нет") {
                hostFragment.navigate(R.id.action_global_menuFragment)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}
