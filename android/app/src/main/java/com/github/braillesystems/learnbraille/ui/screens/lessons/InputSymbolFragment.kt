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
import com.github.braillesystems.learnbraille.data.types.InputSymbol
import com.github.braillesystems.learnbraille.data.db.getDBInstance
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsInputSymbolBinding
import com.github.braillesystems.learnbraille.defaultUser
import com.github.braillesystems.learnbraille.ui.screens.*
import com.github.braillesystems.learnbraille.utils.application
import com.github.braillesystems.learnbraille.utils.updateTitle
import com.github.braillesystems.learnbraille.ui.views.*
import timber.log.Timber

class InputSymbolFragment : AbstractInputLesson(R.string.lessons_help_input_symbol) {

    private lateinit var expectedDots: BrailleDots
    private lateinit var dots: BrailleDotsState
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
        letter.text = step.data.symbol.symbol.toString()
        brailleDots.dotsState.display(step.data.symbol.brailleDots)
        makeIntroLetterToast(step.data.symbol.symbol.toString())

        expectedDots = step.data.symbol.brailleDots
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
            this@InputSymbolFragment, viewModelFactory
        ).get(InputViewModel::class.java)
        buzzer = activity?.getSystemService()


        inputViewModel = viewModel
        lifecycleOwner = this@InputSymbolFragment


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
                getEventIncorrectObserverBlock(step, defaultUser, database, dots) {
                    makeIncorrectLetterToast(step.data.symbol.symbol.toString())
                }
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
            viewModel.getEventPassHintObserver(
                dots, getEventPassHintObserverBlock {
                    makeIntroLetterToast(step.data.symbol.symbol.toString())
                }
            )
        )

    }.root
}
