package com.github.braillesystems.learnbraille.screens.practice

import android.annotation.SuppressLint
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
import com.github.braillesystems.learnbraille.database.getDBInstance
import com.github.braillesystems.learnbraille.databinding.FragmentPracticeBinding
import com.github.braillesystems.learnbraille.screens.*
import com.github.braillesystems.learnbraille.serial.UsbSerial
import com.github.braillesystems.learnbraille.util.application
import com.github.braillesystems.learnbraille.util.updateTitle
import com.github.braillesystems.learnbraille.views.BrailleDotsState
import com.github.braillesystems.learnbraille.views.brailleDots
import com.github.braillesystems.learnbraille.views.dots
import com.github.braillesystems.learnbraille.views.spelling
import timber.log.Timber

class PracticeFragment : AbstractFragmentWithHelp(R.string.practice_help) {

    private lateinit var viewModel: PracticeViewModel
    private lateinit var dots: BrailleDotsState
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
        dots = brailleDots.dots

        val viewModelFactory = PracticeViewModelFactory(dataSource, application) {
            dots.brailleDots
        }
        viewModel = ViewModelProvider(
            this@PracticeFragment, viewModelFactory
        ).get(PracticeViewModel::class.java)
        buzzer = activity?.getSystemService()


        practiceViewModel = viewModel
        lifecycleOwner = this@PracticeFragment


        // Init serial connection with Braille Trainer
        // TODO extract initialization to factory
        // TODO use application.usbManager
        @SuppressLint("WrongConstant") // permit `application.getSystemService(USB_SERVICE)`
        val usbManager = application.getSystemService("usb") as UsbManager
        val filter = IntentFilter().apply {
            addAction(UsbSerial.ACTION_USB_PERMISSION)
            addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED)
            addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        }
        val serial = UsbSerial(usbManager, application)
        application.registerReceiver(serial.broadcastReceiver, filter)


        viewModel.eventCorrect.observe(
            viewLifecycleOwner,
            viewModel.getEventCorrectObserver(dots, buzzer) {
                Timber.i("Handle correct")
                makeCorrectToast()
                updateTitle(title)
            }
        )

        viewModel.eventIncorrect.observe(
            viewLifecycleOwner,
            viewModel.getEventIncorrectObserver(dots, buzzer) {
                Timber.i("Handle incorrect: entered = ${dots.spelling}")
                makeIncorrectLetterToast(viewModel.symbol.value)
                updateTitle(title)
            }
        )

        viewModel.eventHint.observe(
            viewLifecycleOwner,
            viewModel.getEventHintObserver(dots, serial) { expectedDots ->
                Timber.i("Handle hint")
                makeHintDotsToast(expectedDots)
            }
        )

        viewModel.eventPassHint.observe(
            viewLifecycleOwner,
            viewModel.getEventPassHintObserver(dots) {
                Timber.i("Handle pass hint")
                makeIntroLetterToast(viewModel.symbol.value)
            }
        )

        viewModel.symbol.observe(
            viewLifecycleOwner,
            Observer {
                makeIntroLetterToast(it)
            }
        )

    }.root
}
