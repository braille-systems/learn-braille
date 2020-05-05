package com.github.braillesystems.learnbraille.ui.screens.theory

import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.core.text.parseAsHtml
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.entities.InputDots
import com.github.braillesystems.learnbraille.data.entities.spelling
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import com.github.braillesystems.learnbraille.data.repository.StepRepository
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsInputDotsBinding
import com.github.braillesystems.learnbraille.ui.screens.*
import com.github.braillesystems.learnbraille.ui.views.*
import com.github.braillesystems.learnbraille.utils.application
import com.github.braillesystems.learnbraille.utils.checkedBuzz
import com.github.braillesystems.learnbraille.utils.updateTitle
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class InputDotsFragment : AbstractStepFragment(R.string.lessons_help_input_dots) {

    private lateinit var expectedDots: BrailleDots
    private lateinit var dotsState: BrailleDotsState
    private var userTouchedDots: Boolean = false
    private var buzzer: Vibrator? = null
    private val preferenceRepository: PreferenceRepository by inject()
    private lateinit var viewModel: InputViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsInputDotsBinding>(
        inflater,
        R.layout.fragment_lessons_input_dots,
        container,
        false
    ).apply {

        Timber.i("Start initialize input dots fragment")

        updateTitle(getString(R.string.lessons_title_input_dots))
        setHasOptionsMenu(true)

        val (step, courseId) = getStepAndCourseIdArgs()
        val stepRepository: StepRepository by inject { parametersOf(courseId) }
        require(step.data is InputDots)
        infoTextView.text = step.data.text?.parseAsHtml()
            ?: getString(R.string.lessons_input_dots_info_template)
                .format(step.data.dots.spelling)
        brailleDots.dotsState.display(step.data.dots)

        expectedDots = step.data.dots
        userTouchedDots = false
        dotsState = brailleDots.dotsState.apply {
            uncheck()
            clickable(true)
            checkBoxes.forEach { checkBox ->
                checkBox.setOnClickListener {
                    userTouchedDots = true
                }
            }
        }


        val viewModelFactory = InputViewModelFactory(application, expectedDots) {
            dotsState.brailleDots
        }
        viewModel = ViewModelProvider(
            this@InputDotsFragment, viewModelFactory
        ).get(InputViewModel::class.java)
        buzzer = activity?.getSystemService()


        inputViewModel = viewModel
        lifecycleOwner = this@InputDotsFragment


        prevButton.setOnClickListener {
            toPrevStep(step, stepRepository)
        }
        toCurrStepButton.setOnClickListener {
            toCurrentStep(stepRepository)
        }

        viewModel.observeEventCorrect(
            viewLifecycleOwner,
            preferenceRepository,
            dotsState, buzzer
        ) {
            makeCorrectToast()
            toNextStep(step, stepRepository, markThisAsPassed = true)
        }

        viewModel.observeEventIncorrect(
            viewLifecycleOwner,
            preferenceRepository,
            dotsState, buzzer
        ) {
            val notify = {
                makeIncorrectToast()
                buzzer.checkedBuzz(preferenceRepository.incorrectBuzzPattern, preferenceRepository)
            }
            if (userTouchedDots) notify()
            else toNextStep(step, stepRepository, markThisAsPassed = false) { notify() }
        }

        viewModel.observeEventHint(
            viewLifecycleOwner, dotsState
        ) {
            makeHintDotsToast(expectedDots)
        }

        viewModel.observeEventPassHint(
            viewLifecycleOwner, dotsState
        )

    }.root
}
