package com.github.braillesystems.learnbraille.screens.exit

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.databinding.FragmentExitBinding
import com.github.braillesystems.learnbraille.util.updateTitle
import java.util.*
import kotlin.system.exitProcess

open class ExitFragment : Fragment() {

    private lateinit var mTTs:TextToSpeech
    private var mostRecentUtteranceID: String? = null

    private var mSpeechRecognizer: SpeechRecognizer? = null
    private var mSpeechRecognizerIntent: Intent? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentExitBinding>(
        inflater,
        R.layout.fragment_exit,
        container,
        false
    ).apply {

        updateTitle(getString(R.string.exit_question))

        mTTs = TextToSpeech(context, TextToSpeech.OnInitListener {status ->
            if (status == TextToSpeech.SUCCESS) {
                mTTs.language = Locale.UK
                mostRecentUtteranceID = (Random().nextInt() % 9999999).toString() + ""

                val params: MutableMap<String, String?> = HashMap()
                params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = mostRecentUtteranceID
                mTTs.speak(getString(R.string.exit_question), TextToSpeech.QUEUE_FLUSH,
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
                            };
                        }
                    }
                    override fun onError(utteranceId: String) {}
                    override fun onStart(utteranceId: String) {}
                })

            }
        })

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        mSpeechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        mSpeechRecognizerIntent!!.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        mSpeechRecognizerIntent!!.putExtra(
            RecognizerIntent.EXTRA_CALLING_PACKAGE,
            context?.packageName
        )
        mSpeechRecognizer?.setRecognitionListener(SpeechRecognitionListener())


        exitButton.setOnClickListener {
            exitProcess(0)
        }

        continueButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_global_menuFragment)
        )

    }.root

    override fun onDestroy() {
        super.onDestroy()
        mSpeechRecognizer?.destroy()
    }

    protected class SpeechRecognitionListener : RecognitionListener {
        override fun onBeginningOfSpeech() {}
        override fun onBufferReceived(buffer: ByteArray) {}
        override fun onEndOfSpeech() {}
        override fun onError(error: Int) {}
        override fun onEvent(eventType: Int, params: Bundle) {}
        override fun onPartialResults(partialResults: Bundle) {}
        override fun onReadyForSpeech(params: Bundle) {}
        override fun onRmsChanged(rmsdB: Float) {}

        override fun onResults(results: Bundle) {
            val matches =
                results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (matches!![0].equals("да")){
                exitProcess(0)
            }
            if (matches[0].equals("нет")){
                Navigation.createNavigateOnClickListener(R.id.action_global_menuFragment)
            }
        }
    }
}