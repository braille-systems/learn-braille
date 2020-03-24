package ru.spbstu.amd.learnbraille.screens.practice

import android.app.Application
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.braille_dots.view.*
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.LearnBrailleDatabase
import ru.spbstu.amd.learnbraille.databinding.FragmentPracticeBinding
import timber.log.Timber

class PracticeFragment : Fragment() {

    companion object {
        val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
        val INCORRECT_BUZZ_PATTERN = longArrayOf(0, 200)
    }

    private lateinit var viewModel: PracticeViewModel
    private lateinit var viewModelFactory: PracticeViewModelFactory
    private var buzzer: Vibrator? = null

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
            arrayOf(dotButton1, dotButton2, dotButton3, dotButton4, dotButton5, dotButton6)
        }

        viewModelFactory = PracticeViewModelFactory(
            dataSource, application, dotCheckBoxes.map { BrailleDotState(it) }.toTypedArray()
        )
        viewModel = ViewModelProvider(
            this@PracticeFragment, viewModelFactory
        ).get(PracticeViewModel::class.java)

        buzzer = activity?.getSystemService()

        practiceViewModel = viewModel
        lifecycleOwner = this@PracticeFragment

        // TODO remove cast
        // TODO refactor title bar management
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.practice_actionbar_title)

        mainMenuButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_practiceFragment_to_menuFragment
            )
        )

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

    }.root

    private fun makeUnchecked(checkBoxes: Array<CheckBox>) = checkBoxes.forEach {
        if (it.isChecked) {
            it.toggle()
        }
    }
}
