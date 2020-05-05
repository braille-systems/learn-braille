package com.github.braillesystems.learnbraille.ui.screens.theory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.Show
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.data.repository.StepRepository
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsShowSymbolBinding
import com.github.braillesystems.learnbraille.ui.screens.getStepAndCourseIdArgs
import com.github.braillesystems.learnbraille.ui.screens.toCurrentStep
import com.github.braillesystems.learnbraille.ui.screens.toNextStep
import com.github.braillesystems.learnbraille.ui.screens.toPrevStep
import com.github.braillesystems.learnbraille.ui.views.display
import com.github.braillesystems.learnbraille.ui.views.dotsState
import com.github.braillesystems.learnbraille.utils.updateTitle
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class ShowSymbolFragment : AbstractStepFragment(R.string.lessons_help_show_symbol) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsShowSymbolBinding>(
        inflater,
        R.layout.fragment_lessons_show_symbol,
        container,
        false
    ).apply {

        Timber.i("onCreateView")

        updateTitle(getString(R.string.lessons_title_show_symbol))
        setHasOptionsMenu(true)

        val (step, courseId) = getStepAndCourseIdArgs()
        val stepRepository: StepRepository by inject { parametersOf(courseId) }
        require(step.data is Show)
        require(step.data.material.data is Symbol)
        letter.text = step.data.material.data.symbol.toString()
        brailleDots.dotsState.display(step.data.material.data.brailleDots)

        prevButton.setOnClickListener {
            toPrevStep(step, stepRepository)
        }
        nextButton.setOnClickListener {
            toNextStep(step, stepRepository, markThisAsPassed = true)
        }
        toCurrStepButton.setOnClickListener {
            toCurrentStep(stepRepository)
        }

    }.root
}
