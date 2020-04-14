package com.github.braillesystems.learnbraille.screens.practice

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.database.entities.spelling
import com.github.braillesystems.learnbraille.database.getDBInstance
import com.github.braillesystems.learnbraille.databinding.FragmentPracticeBinding
import com.github.braillesystems.learnbraille.screens.*
import com.github.braillesystems.learnbraille.serial.UsbSerial
import com.github.braillesystems.learnbraille.util.application
import com.github.braillesystems.learnbraille.util.updateTitle
import com.github.braillesystems.learnbraille.views.Dots
import com.github.braillesystems.learnbraille.views.brailleDots
import com.github.braillesystems.learnbraille.views.dots
import com.github.braillesystems.learnbraille.views.spelling
import timber.log.Timber

class PracticeFragment : AbstractFragmentWithHelp(R.string.practice_help) {

    private lateinit var viewModel: PracticeViewModel
    private lateinit var dots: Dots
    private var buzzer: Vibrator? = null

    private val title: String
        get() = getString(R.string.practice_actionbar_title_template).format(
            viewModel.nCorrect,
            viewModel.nTries
        )

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

        val dataSource = getDBInstance().symbolDao
        dots = practiceButtons.dots

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
                Toast.makeText(context, getString(R.string.msg_correct), Toast.LENGTH_SHORT).show()
                updateTitle(title)
            }
        )

        viewModel.eventIncorrect.observe(
            viewLifecycleOwner,
            viewModel.getEventIncorrectObserver(dots, buzzer) {
                Timber.i("Handle incorrect: entered = ${dots.spelling}")
                Toast.makeText(context, getString(R.string.msg_incorrect), Toast.LENGTH_SHORT)
                    .show()
                updateTitle(title)
            }
        )

        viewModel.eventHint.observe(
            viewLifecycleOwner,
            viewModel.getEventHintObserver(dots, serial) { expectedDots ->
                Timber.i("Handle hint")
                val toast = getString(R.string.practice_hint_template)
                    .format(expectedDots.spelling)
                Toast.makeText(context, toast, Toast.LENGTH_LONG).show()
            }
        )

        viewModel.eventPassHint.observe(
            viewLifecycleOwner,
            viewModel.getEventPassHintObserver(dots) {
                Timber.i("Handle pass hint")
            }
        )


        updateTitle(title) // Requires viewModel being initialized
        setHasOptionsMenu(true)

    }.root
}
