package com.github.braillesystems.learnbraille.ui.screens.theory.steps

import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
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
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsInputDotsBinding
import com.github.braillesystems.learnbraille.ui.screens.*
import com.github.braillesystems.learnbraille.ui.screens.theory.getStepArg
import com.github.braillesystems.learnbraille.ui.screens.theory.toNextStep
import com.github.braillesystems.learnbraille.ui.screens.theory.toPrevStep
import com.github.braillesystems.learnbraille.ui.views.*
import com.github.braillesystems.learnbraille.utils.announce
import com.github.braillesystems.learnbraille.utils.application
import com.github.braillesystems.learnbraille.utils.checkedBuzz
import org.koin.android.ext.android.inject
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

        val step = getStepArg()
        require(step.data is InputDots)
        val infoText = step.data.text?.parseAsHtml()
            ?: getString(R.string.lessons_show_dots_info_template)
                .format(step.data.dots.spelling)
        infoTextView.text = infoText
        announce(infoText.toString())
        brailleDots.dotsState.display(step.data.dots)

        updateStepTitle(step.lessonId, step.id, R.string.lessons_title_input_dots)
        setHasOptionsMenu(true)

        expectedDots = step.data.dots
        userTouchedDots = false
        dotsState = brailleDots.dotsState.apply {
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
            this@InputDotsFragment, viewModelFactory
        ).get(InputViewModel::class.java)
        buzzer = activity?.getSystemService()


        inputViewModel = viewModel
        lifecycleOwner = this@InputDotsFragment


        prevButton.setOnClickListener {
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
            showHintDotsToast(expectedDots)
            userTouchedDots = true
        }

        viewModel.observeEventPassHint(
            viewLifecycleOwner, dotsState
        )

    }.root
}
