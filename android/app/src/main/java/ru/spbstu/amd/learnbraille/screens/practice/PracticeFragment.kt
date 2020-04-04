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
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.BrailleDot
import ru.spbstu.amd.learnbraille.database.BrailleDotsState
import ru.spbstu.amd.learnbraille.database.LearnBrailleDatabase
import ru.spbstu.amd.learnbraille.databinding.FragmentPracticeBinding
import ru.spbstu.amd.learnbraille.screens.updateTitle
import ru.spbstu.amd.learnbraille.serial.UsbSerial
import ru.spbstu.amd.learnbraille.serial.UsbSerial.Companion.USB_SERVICE
import timber.log.Timber

class PracticeFragment : Fragment() {

    private lateinit var viewModel: PracticeViewModel
    private lateinit var viewModelFactory: PracticeViewModelFactory
    private var buzzer: Vibrator? = null

    private val title: String
        get() = getString(R.string.practice_actionbar_title).format(
            viewModel.nCorrect.value,
            viewModel.nLettersFaced.value
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
        val dotCheckBoxes = practiceButtons.run {
            arrayOf(
                dotButton1, dotButton2, dotButton3,
                dotButton4, dotButton5, dotButton6
            )
        }

        // Init serial connection with Braille Trainer
        @SuppressLint("WrongConstant") // permit `application.getSystemService(USB_SERVICE)`
        val usbManager = application.getSystemService(USB_SERVICE) as UsbManager
        val filter = IntentFilter()
        filter.addAction(UsbSerial.ACTION_USB_PERMISSION)
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        val serial = UsbSerial(usbManager, application);
        application.registerReceiver(serial.broadcastReceiver, filter)

        viewModelFactory = PracticeViewModelFactory(
            dataSource, application, BrailleDotsState(dotCheckBoxes)
        )
        viewModel = ViewModelProvider(
            this@PracticeFragment, viewModelFactory
        ).get(PracticeViewModel::class.java)

        buzzer = activity?.getSystemService()


        practiceViewModel = viewModel
        lifecycleOwner = this@PracticeFragment


        viewModel.eventCorrect.observe(this@PracticeFragment, Observer {
            if (!it) {
                return@Observer
            }

            var toastMessage = getString(R.string.msgCorrect)
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            Timber.i("Handle correct")

            // Use deprecated API to be compatible with old android API levels
            @Suppress("DEPRECATION")
            buzzer?.vibrate(CORRECT_BUZZ_PATTERN, -1)

            makeUnchecked(dotCheckBoxes)
            viewModel.onCorrectComplete()
        })

        hintButton.setOnClickListener {
            viewModel.onHint()
            Toast.makeText(context, viewModel.getDotsString(), Toast.LENGTH_SHORT).show()
            Timber.i("Hint invoked")
            val expectedDots = viewModel.getExpectedDots()
            Timber.i(expectedDots.toString())
            dotCheckBoxes[0].isChecked = expectedDots?.b1 == BrailleDot.F
            dotCheckBoxes[1].isChecked = expectedDots?.b2 == BrailleDot.F
            dotCheckBoxes[2].isChecked = expectedDots?.b3 == BrailleDot.F
            dotCheckBoxes[3].isChecked = expectedDots?.b4 == BrailleDot.F
            dotCheckBoxes[4].isChecked = expectedDots?.b5 == BrailleDot.F
            dotCheckBoxes[5].isChecked = expectedDots?.b6 == BrailleDot.F
            dotCheckBoxes.forEach { it.isClickable = false }

            if (expectedDots != null) {
                serial.trySend(expectedDots)
            }
        }

        viewModel.eventIncorrect.observe(this@PracticeFragment, Observer {
            if (!it) {
                return@Observer
            }

            Toast.makeText(context, "Неправильно!", Toast.LENGTH_SHORT).show()
            Timber.i("Handle incorrect")

            // Use deprecated API to be compatible with old android API levels
            @Suppress("DEPRECATION")
            buzzer?.vibrate(INCORRECT_BUZZ_PATTERN, -1)

            makeUnchecked(dotCheckBoxes)
            viewModel.onIncorrectComplete()
        })


        viewModel.nCorrect.observe(this@PracticeFragment, Observer {
            updateTitle(title)
        })

        viewModel.nLettersFaced.observe(this@PracticeFragment, Observer {
            updateTitle(title)
            dotCheckBoxes.forEach { it.isClickable = true }
        })

        setHasOptionsMenu(true)


        viewModel.eventWaitDBInit.observe(this@PracticeFragment, Observer {
            if (!it) {
                return@Observer
            }
            Toast.makeText(
                context,
                getString(R.string.practice_db_not_initialized_warning),
                Toast.LENGTH_LONG
            ).show()
            findNavController().navigate(R.id.action_practiceFragment_to_menuFragment)
        })

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

    private fun makeUnchecked(checkBoxes: Array<CheckBox>) = checkBoxes
        .forEach {
            if (it.isChecked) {
                it.toggle()
            }
        }

    companion object {
        val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
        val INCORRECT_BUZZ_PATTERN = longArrayOf(0, 200)
    }
}
