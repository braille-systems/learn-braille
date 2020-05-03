package com.github.braillesystems.learnbraille.ui.screens.practice

import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.db.getDBInstance
import com.github.braillesystems.learnbraille.databinding.FragmentPracticeBinding
import com.github.braillesystems.learnbraille.ui.screens.*
import com.github.braillesystems.learnbraille.ui.serial.UsbParser
import com.github.braillesystems.learnbraille.ui.serial.UsbSignalHandler
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsState
import com.github.braillesystems.learnbraille.ui.views.brailleDots
import com.github.braillesystems.learnbraille.ui.views.dotsState
import com.github.braillesystems.learnbraille.utils.application
import com.github.braillesystems.learnbraille.utils.updateTitle
import timber.log.Timber

class PracticeFragment : AbstractFragmentWithHelp(R.string.practice_help) {

    private lateinit var viewModel: PracticeViewModel
    private lateinit var dotsState: BrailleDotsState
    private var buzzer: Vibrator? = null

    private val title: String
        get() = getString(R.string.practice_actionbar_title_template).let {
            if (::viewModel.isInitialized) it.format(
                viewModel.nCorrect,
                viewModel.nTries
            )
            else it.format(0, 0)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentPracticeBinding>(
        inflater,
        R.layout.fragment_practice,
        container,
        false
    ).apply {

        Timber.i("Start initialize practice fragment")

        updateTitle(title)
        setHasOptionsMenu(true)

        val dataSource = getDBInstance().symbolDao
        dotsState = brailleDots.dotsState

        val viewModelFactory = PracticeViewModelFactory(dataSource, application) {
            dotsState.brailleDots
        }
        viewModel = ViewModelProvider(
            this@PracticeFragment, viewModelFactory
        ).get(PracticeViewModel::class.java)
        buzzer = activity?.getSystemService()


        practiceViewModel = viewModel
        lifecycleOwner = this@PracticeFragment


        viewModel.observeEventCorrect(
            viewLifecycleOwner, application, dotsState, buzzer
        ) {
            makeCorrectToast()
            updateTitle(title)
        }

        viewModel.observeEventIncorrect(
            viewLifecycleOwner, application, dotsState, buzzer
        ) {
            makeIncorrectLetterToast(viewModel.symbol.value)
            updateTitle(title)
        }

        viewModel.observeEventHint(
            viewLifecycleOwner, dotsState
        ) { expectedDots ->
            makeHintDotsToast(expectedDots)
        }

        viewModel.observeEventPassHint(
            viewLifecycleOwner, dotsState
        ) {
            makeIntroLetterToast(viewModel.symbol.value)
        }

        viewModel.symbol.observe(
            viewLifecycleOwner,
            Observer {
                makeIntroLetterToast(it)
            }
        )

        UsbParser.setSignalHandler(UsbPracticeHandler())

    }.root
}

class UsbPracticeHandler : UsbSignalHandler {
    override fun onJoystickRight() {
    }
}



