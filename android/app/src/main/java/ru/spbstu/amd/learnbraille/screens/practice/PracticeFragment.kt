package ru.spbstu.amd.learnbraille.screens.practice

import android.annotation.SuppressLint
import android.app.Application
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.os.Vibrator
import android.view.*
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.braille_dots.view.*
import ru.spbstu.amd.learnbraille.CORRECT_BUZZ_PATTERN
import ru.spbstu.amd.learnbraille.INCORRECT_BUZZ_PATTERN
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.*
import ru.spbstu.amd.learnbraille.databinding.FragmentPracticeBinding
import ru.spbstu.amd.learnbraille.screens.updateTitle
import ru.spbstu.amd.learnbraille.serial.UsbSerial
import timber.log.Timber

class PracticeFragment : Fragment() {

    private lateinit var viewModel: PracticeViewModel
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

        val application: Application = requireNotNull(activity).application
        val dataSource = LearnBrailleDatabase.getInstance(application).symbolDao
        val dotCheckBoxes: Array<CheckBox> = practiceButtons.run {
            arrayOf(
                dotButton1, dotButton2, dotButton3,
                dotButton4, dotButton5, dotButton6
            )
        }

        val viewModelFactory = PracticeViewModelFactory(
            dataSource, application, BrailleDotsState(dotCheckBoxes)
        )
        viewModel = ViewModelProvider(
            this@PracticeFragment, viewModelFactory
        ).get(PracticeViewModel::class.java)
        buzzer = activity?.getSystemService()


        practiceViewModel = viewModel
        lifecycleOwner = this@PracticeFragment


        // Init serial connection with Braille Trainer
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


        viewModel.eventCorrect.observe(viewLifecycleOwner, Observer {
            if (!it) {
                return@Observer
            }

            Toast.makeText(context, getString(R.string.msgCorrect), Toast.LENGTH_SHORT).show()
            Timber.i("Handle correct")

            // Use deprecated API to be compatible with old android API levels
            @Suppress("DEPRECATION")
            buzzer?.vibrate(CORRECT_BUZZ_PATTERN, -1)

            makeUnchecked(dotCheckBoxes)
            updateTitle(title)
            viewModel.onCorrectComplete()
        })

        viewModel.eventIncorrect.observe(viewLifecycleOwner, Observer {
            if (!it) {
                return@Observer
            }

            Toast.makeText(context, getString(R.string.msgIncorrect), Toast.LENGTH_SHORT).show()
            Timber.i("Handle incorrect")

            // Use deprecated API to be compatible with old android API levels
            @Suppress("DEPRECATION")
            buzzer?.vibrate(INCORRECT_BUZZ_PATTERN, -1)

            makeUnchecked(dotCheckBoxes)
            updateTitle(title)
            viewModel.onIncorrectComplete()
        })

        viewModel.eventHint.observe(viewLifecycleOwner, Observer { expectedDots: BrailleDots? ->
            if (expectedDots == null) {
                return@Observer
            }

            val toast = getString(R.string.practice_hint_template).format(expectedDots.spell)
            Toast.makeText(context, toast, Toast.LENGTH_LONG).show()
            (dotCheckBoxes zip expectedDots.list).forEach { (checkBox, dot) ->
                checkBox.isChecked = dot == BrailleDot.F
                checkBox.isClickable = false
            }
            serial.trySend(expectedDots)
            viewModel.onHintComplete()
        })

        viewModel.eventPassHint.observe(viewLifecycleOwner, Observer {
            if (!it) {
                return@Observer
            }
            dotCheckBoxes.forEach { checkBox ->
                checkBox.isChecked = false
                checkBox.isClickable = true
            }
            viewModel.onPassHintComplete()
        })


        updateTitle(title)
        setHasOptionsMenu(true)

    }.root

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.help_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = super.onOptionsItemSelected(item).also {
        when (item.itemId) {
            R.id.help -> {
                val action = PracticeFragmentDirections.actionPracticeFragmentToHelpFragment()
                action.helpMessage = getString(R.string.practice_help)
                findNavController().navigate(action)
            }
        }
    }
}

private fun makeUnchecked(checkBoxes: Array<CheckBox>) =
    checkBoxes.forEach {
        it.isChecked = false
    }
