package com.github.braillesystems.learnbraille.ui.screens.theory.steps.input

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Vibrator
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModelProvider
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.BaseInput
import com.github.braillesystems.learnbraille.data.entities.StepData
import com.github.braillesystems.learnbraille.ui.screens.*
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.AbstractStepFragment
import com.github.braillesystems.learnbraille.ui.screens.theory.toNextStep
import com.github.braillesystems.learnbraille.ui.announceCorrect
import com.github.braillesystems.learnbraille.ui.showHintToast
import com.github.braillesystems.learnbraille.ui.announceIncorrect
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
            softBlock = {
                announceCorrect()
                val colorFrom = context?.let { ContextCompat.getColor(it, R.color.colorPrimary) }
                val colorTo = context?.let { ContextCompat.getColor(it, R.color.colorGreen) }
                animateButton(binding.rightButton, colorFrom, colorTo)
            }
        )

        viewModel.observeEventHint(
            viewLifecycleOwner, { dotsState }
        ) {
            showHintToast(expectedDots)
            userTouchedDots = true
        }

        viewModel.observeEventIncorrect(
            viewLifecycleOwner, { dotsState }
        ) {
            fun notify() {
                announceIncorrect(data)
                buzzer.checkedBuzz(
                    preferenceRepository.incorrectBuzzPattern,
                    preferenceRepository
                )
                val colorFrom = context?.let { ContextCompat.getColor(it, R.color.colorPrimary) }
                val colorTo = context?.let { ContextCompat.getColor(it, R.color.colorRed) }
                animateButton(binding.rightButton, colorFrom, colorTo)
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

    protected open fun announceIncorrect(data: StepData) {
        announceIncorrect()
    }

    protected open fun onPassHint(data: StepData) = Unit

    private fun animateButton(button: Button?, colorFrom: Int?, colorTo: Int?) {
        val duration = 1000
        ObjectAnimator.ofObject(
            button,
            "backgroundColor",
            ArgbEvaluator(),
            colorFrom,
            colorTo,
            colorFrom
        )
            .setDuration(duration.toLong())
            .start()
    }
}
