package com.github.braillesystems.learnbraille.ui.screens.lessons

import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.types.BrailleDots
import com.github.braillesystems.learnbraille.data.types.InputDots
import com.github.braillesystems.learnbraille.data.types.spelling
import com.github.braillesystems.learnbraille.data.db.getDBInstance
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsInputDotsBinding
import com.github.braillesystems.learnbraille.defaultUser
import com.github.braillesystems.learnbraille.ui.screens.getEventCorrectObserver
import com.github.braillesystems.learnbraille.ui.screens.getEventHintObserver
import com.github.braillesystems.learnbraille.ui.screens.getEventIncorrectObserver
import com.github.braillesystems.learnbraille.ui.screens.getEventPassHintObserver
import com.github.braillesystems.learnbraille.utils.application
import com.github.braillesystems.learnbraille.utils.updateTitle
import com.github.braillesystems.learnbraille.ui.views.*
import timber.log.Timber

class InputDotsFragment : AbstractInputLesson(R.string.lessons_help_input_dots) {

    private lateinit var expectedDots: BrailleDots
    private lateinit var dots: BrailleDotsState
    private var buzzer: Vibrator? = null

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

        val step = getStepArg()
        require(step.data is InputDots)
        titleTextView.text = step.title
        infoTextView.text = step.data.text
            ?: getString(R.string.lessons_input_dots_info_template)
                .format(step.data.dots.spelling)
        brailleDots.dotsState.display(step.data.dots)

        expectedDots = step.data.dots
        userTouchedDots = false
        dots = brailleDots.dotsState.apply {
            uncheck()
            clickable(true)
            checkBoxes.forEach { checkBox ->
                checkBox.setOnClickListener {
                    userTouchedDots = true
                }
            }
        }


        val viewModelFactory = InputViewModelFactory(application, expectedDots) {
            dots.brailleDots
        }
        viewModel = ViewModelProvider(
            this@InputDotsFragment, viewModelFactory
        ).get(InputViewModel::class.java)
        buzzer = activity?.getSystemService()


        inputViewModel = viewModel
        lifecycleOwner = this@InputDotsFragment


        val database = getDBInstance()

        prevButton.setOnClickListener(getPrevButtonListener(step, defaultUser, database))
        toCurrStepButton.setOnClickListener(getToCurrStepListener(defaultUser, database))

        viewModel.eventCorrect.observe(
            viewLifecycleOwner,
            viewModel.getEventCorrectObserver(
                dots, buzzer,
                getEventCorrectObserverBlock(step, defaultUser, database)
            )
        )

        viewModel.eventIncorrect.observe(
            viewLifecycleOwner,
            viewModel.getEventIncorrectObserver(
                dots, buzzer,
                getEventIncorrectObserverBlock(step, defaultUser, database, dots)
            )
        )

        viewModel.eventHint.observe(
            viewLifecycleOwner,
            viewModel.getEventHintObserver(
                dots, null, /*TODO serial */
                getEventHintObserverBlock()
            )
        )

        viewModel.eventPassHint.observe(
            viewLifecycleOwner,
            viewModel.getEventPassHintObserver(dots, getEventPassHintObserverBlock())
        )

    }.root
}
