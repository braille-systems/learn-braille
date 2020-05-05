package com.github.braillesystems.learnbraille.ui.screens.practice

import android.content.IntentFilter
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import com.github.braillesystems.learnbraille.databinding.FragmentPracticeBinding
import com.github.braillesystems.learnbraille.toast
import com.github.braillesystems.learnbraille.ui.screens.*
import com.github.braillesystems.learnbraille.ui.serial.UsbSerial
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsState
import com.github.braillesystems.learnbraille.ui.views.brailleDots
import com.github.braillesystems.learnbraille.ui.views.dotsState
import com.github.braillesystems.learnbraille.utils.application
import com.github.braillesystems.learnbraille.utils.updateTitle
import com.github.braillesystems.learnbraille.utils.usbManager
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class CardFragment :
    AbstractFragmentWithHelp(R.string.practice_help),
    KoinComponent {

    private val preferences: PreferenceRepository by inject()

    private lateinit var viewModel: CardViewModel
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

        Timber.i("onCreateView")

        updateTitle(title)
        setHasOptionsMenu(true)

        dotsState = brailleDots.dotsState

        val viewModelFactory: CardViewModelFactory by inject {
            parametersOf({ dotsState.brailleDots })
        }
        viewModel = ViewModelProvider(
            this@CardFragment, viewModelFactory
        ).get(CardViewModel::class.java)
        buzzer = activity?.getSystemService()


        cardViewModel = viewModel
        lifecycleOwner = this@CardFragment


        // Init serial connection with Braille Trainer
        // TODO extract initialization to factory
        val filter = IntentFilter().apply {
            addAction(UsbSerial.ACTION_USB_PERMISSION)
            addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED)
            addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        }
        val serial = UsbSerial(application.usbManager, application)
        application.registerReceiver(serial.broadcastReceiver, filter)


        viewModel.observeEventCorrect(
            viewLifecycleOwner, preferences, dotsState, buzzer
        ) {
            makeCorrectToast()
            updateTitle(title)
        }

        viewModel.observeEventIncorrect(
            viewLifecycleOwner, preferences, dotsState, buzzer
        ) {
            viewModel.symbol.value?.let { symbol ->
                makeIncorrectLetterToast(symbol)
            } ?: toast(getString(R.string.input_loading))
            updateTitle(title)
        }

        viewModel.observeEventHint(
            viewLifecycleOwner, dotsState, serial
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
            Observer { symbol: String ->
                makeIntroLetterToast(symbol)
            }
        )

    }.root
}
