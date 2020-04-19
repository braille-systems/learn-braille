package com.github.braillesystems.learnbraille.screens.exit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.databinding.FragmentExitBinding
import com.github.braillesystems.learnbraille.util.application
import com.github.braillesystems.learnbraille.util.updateTitle
import java.util.*
import kotlin.system.exitProcess

class ExitFragment : Fragment() {

    private lateinit var mTTs:TextToSpeech

    private val REQUEST_CODE_SPEECH_INPUT = 100

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
            if (status != TextToSpeech.ERROR) {
                mTTs.language = Locale.UK
            }
        })

        mTTs.speak(getString(R.string.exit_question), TextToSpeech.QUEUE_FLUSH, null)

        exitButton.setOnClickListener {
            exitProcess(0)
        }

        continueButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_global_menuFragment)
        )

        speak()
    }.root


    private fun speak(){
        val mIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)

        mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        mIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.exit_question))
        startActivityForResult(mIntent, REQUEST_CODE_SPEECH_INPUT)
    }

    override fun OnActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        //super.onActivityResult(requestCode, resultCode, data)
        when (requestCode){
            REQUEST_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    if (result?.get(0)?.equals(getString(R.string.exit_voice_yes))!!){
                        exitProcess(0)
                    } else if (result.get(0)?.equals(getString(R.string.exit_voice_no))!!){
                        Navigation.createNavigateOnClickListener(R.id.action_global_menuFragment)
                    }
                }
            }
        }
    }
}
