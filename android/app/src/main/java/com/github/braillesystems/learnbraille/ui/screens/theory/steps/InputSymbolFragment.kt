package com.github.braillesystems.learnbraille.ui.screens.theory.steps

import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.entities.Input
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsInputSymbolBinding
import com.github.braillesystems.learnbraille.ui.screens.*
import com.github.braillesystems.learnbraille.ui.screens.theory.getStepArg
import com.github.braillesystems.learnbraille.ui.screens.theory.toCurrentStep
import com.github.braillesystems.learnbraille.ui.screens.theory.toNextStep
import com.github.braillesystems.learnbraille.ui.screens.theory.toPrevStep
import com.github.braillesystems.learnbraille.ui.views.*
import com.github.braillesystems.learnbraille.utils.announceByAccessibility
import com.github.braillesystems.learnbraille.utils.application
import com.github.braillesystems.learnbraille.utils.checkedBuzz
import org.koin.android.ext.android.inject
import timber.log.Timber

class InputSymbolFragment : AbstractStepFragment(R.string.lessons_help_input_symbol) {

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
    ) = DataBindingUtil.inflate<FragmentLessonsInputSymbolBinding>(
        inflater,
        R.layout.fragment_lessons_input_symbol,
        container,
        false
    ).apply {

        Timber.i("Initialize input symbol fragment")

        val step = getStepArg()
        require(step.data is Input)
        require(step.data.material.data is Symbol)
        val symbol = step.data.material.data
        letter.text = symbol.symbol.toString()
        brailleDots.dotsState.display(symbol.brailleDots)
        announceByAccessibility(introStringNotNullLogged(step.data.material))

        updateStepTitle(step.lessonId, step.id, R.string.lessons_title_input_symbol)
        setHasOptionsMenu(true)

        expectedDots = symbol.brailleDots
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


        val viewModelFactory =
            InputViewModelFactory(
                application,
                expectedDots
            ) {
                dotsState.brailleDots
            }
        viewModel = ViewModelProvider(
            this@InputSymbolFragment, viewModelFactory
        ).get(InputViewModel::class.java)
        buzzer = activity?.getSystemService()


        inputViewModel = viewModel
        lifecycleOwner = this@InputSymbolFragment


        prevButton.setOnClickListener {
            toPrevStep(step)
        }
        toCurrStepButton.setOnClickListener {
            toCurrentStep(step.courseId)
        }

        viewModel.observeEventCorrect(
            viewLifecycleOwner,
            preferenceRepository,
            dotsState, buzzer
        ) {
            showCorrectToast()
            toNextStep(step, markThisAsPassed = true)
        }

        viewModel.observeEventIncorrect(
            viewLifecycleOwner,
            preferenceRepository,
            dotsState
        ) {
            val notify = {
                showIncorrectToast(step.data.material)
                buzzer.checkedBuzz(preferenceRepository.incorrectBuzzPattern, preferenceRepository)
            }
            if (userTouchedDots) notify()
            else toNextStep(step, markThisAsPassed = false) { notify() }
        }

        viewModel.observeEventHint(
            viewLifecycleOwner, dotsState
        ) {
            showHintDotsToast(expectedDots)
            userTouchedDots = true
        }

        viewModel.observeEventPassHint(
            viewLifecycleOwner, dotsState
        ) {
            announceByAccessibility(introStringNotNullLogged(step.data.material))
        }

    }.root
}
