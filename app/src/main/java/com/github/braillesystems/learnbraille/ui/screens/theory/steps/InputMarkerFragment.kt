package com.github.braillesystems.learnbraille.ui.screens.theory.steps

import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.entities.Input
import com.github.braillesystems.learnbraille.data.entities.MarkerSymbol
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsInputMarkerBinding
import com.github.braillesystems.learnbraille.res.inputMarkerPrintRules
import com.github.braillesystems.learnbraille.ui.screens.observeCheckedOnFly
import com.github.braillesystems.learnbraille.ui.screens.observeEventHint
import com.github.braillesystems.learnbraille.ui.screens.observeEventIncorrect
import com.github.braillesystems.learnbraille.ui.screens.observeEventPassHint
import com.github.braillesystems.learnbraille.ui.screens.theory.getStepArg
import com.github.braillesystems.learnbraille.ui.screens.theory.toNextStep
import com.github.braillesystems.learnbraille.ui.screens.theory.toPrevStep
import com.github.braillesystems.learnbraille.ui.showCorrectToast
import com.github.braillesystems.learnbraille.ui.showHintToast
import com.github.braillesystems.learnbraille.ui.showIncorrectToast
import com.github.braillesystems.learnbraille.ui.views.*
import com.github.braillesystems.learnbraille.utils.*
import timber.log.Timber

class InputMarkerFragment : AbstractStepFragment(R.string.lessons_help_input_marker) {

    private lateinit var expectedDots: BrailleDots
    private lateinit var dotsState: BrailleDotsState
    private var userTouchedDots: Boolean = false
    private var buzzer: Vibrator? = null
    private lateinit var viewModel: InputViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsInputMarkerBinding>(
        inflater,
        R.layout.fragment_lessons_input_marker,
        container,
        false
    ).also { binding ->

        Timber.i("Start initialize input dots fragment")

        val step = getStepArg()
        require(step.data is Input)
        initialize(
            step,
            binding.prevButton,
            binding.nextButton,
            binding.hintButton
        )

        val data = step.data.material.data
        require(data is MarkerSymbol)
        val infoText = contextNotNull.inputMarkerPrintRules[data.type]
            ?: error("No input print rules for marker: ${data.type}")
        setText(
            text = infoText,
            infoTextView = binding.infoTextView
        )
        checkedAnnounce(infoText)
        binding.brailleDots.dotsState.display(data.brailleDots)

        updateTitle(getString(R.string.lessons_title_input_symbol))

        expectedDots = data.brailleDots
        userTouchedDots = false
        dotsState = binding.brailleDots.dotsState.apply {
            uncheck()
            clickable(true)
            subscribe(View.OnClickListener {
                userTouchedDots = true
                viewModel.onSoftCheck()
            })
        }


        val viewModelFactory = InputViewModelFactory(application, expectedDots) {
            dotsState.brailleDots
        }
        viewModel = ViewModelProvider(
            this@InputMarkerFragment, viewModelFactory
        ).get(InputViewModel::class.java)
        buzzer = activity?.getSystemService()


        binding.inputViewModel = viewModel
        binding.lifecycleOwner = this@InputMarkerFragment


        binding.prevButton.setOnClickListener {
            toPrevStep(step)
        }

        viewModel.observeCheckedOnFly(
            viewLifecycleOwner, dotsState, buzzer,
            block = { toNextStep(step, markThisAsPassed = true) },
            softBlock = ::showCorrectToast
        )

        viewModel.observeEventIncorrect(
            viewLifecycleOwner, dotsState
        ) {
            val notify = {
                showIncorrectToast()
                buzzer.checkedBuzz(preferenceRepository.incorrectBuzzPattern, preferenceRepository)
            }
            if (userTouchedDots) notify()
            else toNextStep(step, markThisAsPassed = false) { notify() }
        }

        viewModel.observeEventHint(
            viewLifecycleOwner, dotsState
        ) {
            showHintToast(expectedDots)
            userTouchedDots = true
        }

        viewModel.observeEventPassHint(
            viewLifecycleOwner, dotsState
        )

    }.root
}
