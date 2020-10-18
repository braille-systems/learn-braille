package com.github.braillesystems.learnbraille.ui.screens.theory.steps.input

import android.os.Vibrator
import android.view.View
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModelProvider
import com.github.braillesystems.learnbraille.data.entities.BaseInput
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.entities.StepData
import com.github.braillesystems.learnbraille.ui.screens.*
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.AbstractStepFragment
import com.github.braillesystems.learnbraille.ui.screens.theory.toNextStep
import com.github.braillesystems.learnbraille.ui.showCorrectToast
import com.github.braillesystems.learnbraille.ui.showHintToast
import com.github.braillesystems.learnbraille.ui.showIncorrectToast
import com.github.braillesystems.learnbraille.ui.views.*
import com.github.braillesystems.learnbraille.utils.application
import com.github.braillesystems.learnbraille.utils.checkedBuzz

abstract class AbstractInputStepFragment(helpMsgId: HelpMsgId) : AbstractStepFragment(helpMsgId) {

    // This value can change during ViewModel lifetime (ViewModelProvider does not call
    // ViewModelFactory each time onCreateView runs). And once created ViewModel
    // should be able to use up to date dotsState.
    private lateinit var dotsState: BrailleDotsState

    protected lateinit var viewModel: InputViewModel
        private set

    override fun iniStepHelper() {
        val data = step.data
        require(data is BaseInput)
        val expectedDots = data.brailleDots

        var userTouchedDots = false

        dotsState = stepBinding.brailleDotsInfo!!.view.dotsState.apply {
            uncheck()
            clickable(true)
            subscribe(View.OnClickListener {
                viewModel.onSoftCheck()
                userTouchedDots = true
            })
        }

        val viewModelFactory = InputViewModelFactory(
            application, expectedDots
        ) { dotsState.brailleDots }

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(InputViewModel::class.java)

        val buzzer: Vibrator? = activity?.getSystemService()

        stepBinding.flipButton?.setOnClickListener {
            dotsState = stepBinding.brailleDotsInfo!!.view.reflect().apply {
                subscribe(View.OnClickListener {
                    viewModel.onSoftCheck()
                    userTouchedDots = true
                })
                if (viewModel.state == DotsChecker.State.HINT) {
                    display(expectedDots)
                }
            }
        }

        viewModel.observeCheckedOnFly(
            viewLifecycleOwner, { dotsState }, buzzer,
            block = { toNextStep(step, markThisAsPassed = true) },
            softBlock = { showCorrectToast() }
        )

        viewModel.observeEventHint(
            viewLifecycleOwner, { dotsState }
        ) {
            toastHint(expectedDots)
            userTouchedDots = true
        }

        viewModel.observeEventIncorrect(
            viewLifecycleOwner, { dotsState }
        ) {
            fun notify() {
                toastIncorrect(data)
                buzzer.checkedBuzz(
                    preferenceRepository.incorrectBuzzPattern,
                    preferenceRepository
                )
            }
            if (userTouchedDots) notify()
            else toNextStep(step, markThisAsPassed = false) { notify() }
        }

        viewModel.observeEventPassHint(
            viewLifecycleOwner, { dotsState }
        ) {
            onPassHint(data)
        }

        if (viewModel.state == DotsChecker.State.HINT) {
            expectedDots.let { dotsState.display(it) }
        }
    }

    protected open fun toastIncorrect(data: StepData) {
        showIncorrectToast()
    }

    protected open fun toastHint(expectedDots: BrailleDots) {
        showHintToast(expectedDots)
    }

    protected open fun onPassHint(data: StepData) = Unit
}
