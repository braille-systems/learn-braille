package ru.spbstu.amd.learnbraille.screens.practice

import android.app.Application
import android.os.Bundle
import android.os.Vibrator
import android.view.*
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.braille_dots.view.*
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.BrailleDotsState
import ru.spbstu.amd.learnbraille.database.LearnBrailleDatabase
import ru.spbstu.amd.learnbraille.databinding.FragmentPracticeBinding
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

            Toast.makeText(context, "Correct!", Toast.LENGTH_SHORT).show()
            Timber.i("Handle correct")

            // Use deprecated API to be compatible with old android API levels
            @Suppress("DEPRECATION")
            buzzer?.vibrate(CORRECT_BUZZ_PATTERN, -1)

            makeUnchecked(dotCheckBoxes)
            viewModel.onCorrectComplete()
        })

        viewModel.eventIncorrect.observe(this@PracticeFragment, Observer {
            if (!it) {
                return@Observer
            }

            Toast.makeText(context, "Incorrect!", Toast.LENGTH_SHORT).show()
            Timber.i("Handle incorrect")

            // Use deprecated API to be compatible with old android API levels
            @Suppress("DEPRECATION")
            buzzer?.vibrate(INCORRECT_BUZZ_PATTERN, -1)

            makeUnchecked(dotCheckBoxes)
            viewModel.onIncorrectComplete()
        })


        viewModel.nCorrect.observe(this@PracticeFragment, Observer {
            updateTitle()
        })

        viewModel.nLettersFaced.observe(this@PracticeFragment, Observer {
            updateTitle()
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

    private fun updateTitle() {
        (activity as AppCompatActivity)
            .supportActionBar
            ?.title = title
    }

    companion object {
        val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
        val INCORRECT_BUZZ_PATTERN = longArrayOf(0, 200)
    }
}
