package com.github.braillesystems.learnbraille.screens.lessons

import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.database.entities.BrailleDots
import com.github.braillesystems.learnbraille.database.entities.InputSymbol
import com.github.braillesystems.learnbraille.database.entities.spelling
import com.github.braillesystems.learnbraille.database.getDBInstance
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsInputSymbolBinding
import com.github.braillesystems.learnbraille.defaultUser
import com.github.braillesystems.learnbraille.screens.getEventCorrectObserver
import com.github.braillesystems.learnbraille.screens.getEventHintObserver
import com.github.braillesystems.learnbraille.screens.getEventIncorrectObserver
import com.github.braillesystems.learnbraille.screens.getEventPassHintObserver
import com.github.braillesystems.learnbraille.util.application
import com.github.braillesystems.learnbraille.util.updateTitle
import com.github.braillesystems.learnbraille.views.*
import timber.log.Timber

class InputSymbolFragment : AbstractLesson(R.string.lessons_help_input_symbol) {

    private lateinit var viewModel: InputViewModel
    private lateinit var expectedDots: BrailleDots
    private lateinit var dots: Dots
    private var buzzer: Vibrator? = null

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

        updateTitle(getString(R.string.lessons_title_input_symbol))
        setHasOptionsMenu(true)

        val step = getStepArg()
        require(step.data is InputSymbol)
        titleTextView.text = step.title
        infoTextView.text = step.data.symbol.symbol.toString()
        brailleDots.dots.display(step.data.symbol.brailleDots)

        expectedDots = step.data.symbol.brailleDots
        dots = brailleDots.dots.apply {
            uncheck()
            clickable(true)
        }


        val viewModelFactory = InputViewModelFactory(application, expectedDots) {
            dots.brailleDots
        }
        viewModel = ViewModelProvider(
            this@InputSymbolFragment, viewModelFactory
        ).get(InputViewModel::class.java)
        buzzer = activity?.getSystemService()


        inputViewModel = viewModel
        lifecycleOwner = this@InputSymbolFragment


        val database = getDBInstance()

        prevButton.setOnClickListener {
            navigateToPrevStep(database.stepDao, step)
        }

        toCurrStepButton.setOnClickListener {
            navigateToCurrentStep(
                database.stepDao, defaultUser
            )
        }

        viewModel.eventCorrect.observe(
            viewLifecycleOwner,
            viewModel.getEventCorrectObserver(dots, buzzer) {
                Timber.i("Handle correct")
                Toast.makeText(context, getString(R.string.msg_correct), Toast.LENGTH_SHORT).show()
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
                Toast.makeText(context, getString(R.string.msg_incorrect), Toast.LENGTH_SHORT)
                    .show()
                navigateToNextStep(
                    database.stepDao, step, defaultUser
                )
            }
        )

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
