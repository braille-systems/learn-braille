package com.github.braillesystems.learnbraille.ui.screens.exit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.databinding.FragmentExitBinding
import com.github.braillesystems.learnbraille.utils.SpeechRecognition
import com.github.braillesystems.learnbraille.utils.checkedAnnounce
import com.github.braillesystems.learnbraille.utils.navigate
import com.github.braillesystems.learnbraille.utils.updateTitle
import org.koin.android.ext.android.get
import kotlin.system.exitProcess

class ExitFragment : Fragment() {

    private lateinit var recognizer: SpeechRecognition

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

        val title: String = getString(R.string.exit_question)
        updateTitle(title)
        checkedAnnounce(title)

        recognizer = SpeechRecognition(this@ExitFragment, get())

        exitButton.setOnClickListener {
            exitProcess(0)
        }

        continueButton.setOnClickListener {
            navigate(R.id.action_global_menuFragment)
        }

    }.root

    override fun onDestroy() {
        super.onDestroy()
        recognizer.onDestroy()
    }
}
