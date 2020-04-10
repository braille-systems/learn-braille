package ru.spbstu.amd.learnbraille.screens.lessons

import android.app.Application
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.LearnBrailleDatabase
import ru.spbstu.amd.learnbraille.database.entities.BrailleDots
import ru.spbstu.amd.learnbraille.database.entities.InputDots
import ru.spbstu.amd.learnbraille.database.entities.spelling
import ru.spbstu.amd.learnbraille.databinding.FragmentLessonsDotsBinding
import ru.spbstu.amd.learnbraille.defaultUser
import ru.spbstu.amd.learnbraille.screens.*
import ru.spbstu.amd.learnbraille.views.Dots
import ru.spbstu.amd.learnbraille.views.brailleDots
import ru.spbstu.amd.learnbraille.views.spelling
import timber.log.Timber

class InputDotsFragment : AbstractLesson(R.string.lessons_help_input_dots) {

    private lateinit var viewModel: InputViewModel
    private lateinit var expectedDots: BrailleDots
    private lateinit var dots: Dots
    private var buzzer: Vibrator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsDotsBinding>(
        inflater,
        R.layout.fragment_lessons_dots,
        container,
        false
    ).apply {

        Timber.i("Start initialize input dots fragment")

        updateTitle(getString(R.string.lessons_title_input_dots))
        setHasOptionsMenu(true)

        val step = getStepArg()
        require(step.data is InputDots)
        titleView.text = step.title
        infoText.text = getString(R.string.lessons_input_dots_info_template)
            .format(step.data.dots.spelling)
        expectedDots = step.data.dots
        dots = brailleDots.dots


        val application: Application = requireNotNull(activity).application
        val viewModelFactory = InputViewModelFactory(application, expectedDots) {
            dots.brailleDots
        }
        viewModel = ViewModelProvider(
            this@InputDotsFragment, viewModelFactory
        ).get(InputViewModel::class.java)
        buzzer = activity?.getSystemService()


        inputViewModel = viewModel
        lifecycleOwner = this@InputDotsFragment


        val database = LearnBrailleDatabase.getInstance(application)

        prevButton.setOnClickListener {
            navigateToPrevStep(database.stepDao, step)
        }


        viewModel.eventCorrect.observe(
            viewLifecycleOwner,
            viewModel.getEventCorrectObserver(dots, buzzer) {
                Timber.i("Handle correct")
                Toast.makeText(context, getString(R.string.msgCorrect), Toast.LENGTH_SHORT).show()
                navigateToNextStep(
                    database.stepDao, step, defaultUser,
                    database.userPassedStepDao
                )
            }
        )

        viewModel.eventIncorrect.observe(
            viewLifecycleOwner,
            viewModel.getEventIncorrectObserver(dots, buzzer) {
                Timber.i("Handle incorrect: entered = ${dots.spelling}")
                Toast.makeText(context, getString(R.string.msgIncorrect), Toast.LENGTH_SHORT).show()
            }
        )

        // TODO connect hint

        viewModel.eventHint.observe(
            viewLifecycleOwner,
            viewModel.getEventHintObserver(dots /*, TODO serial */) { expectedDots ->
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

    }.root
}
