package com.github.braillesystems.learnbraille.ui.screens.theory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.ShowDots
import com.github.braillesystems.learnbraille.data.entities.spelling
import com.github.braillesystems.learnbraille.data.repository.StepRepository
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsShowDotsBinding
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

class ShowDotsFragment : AbstractStepFragment(R.string.lessons_help_show_dots) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsShowDotsBinding>(
        inflater,
        R.layout.fragment_lessons_show_dots,
        container,
        false
    ).apply {

        Timber.i("Initialize show dots fragment")

        updateTitle(getString(R.string.lessons_title_show_dots))
        setHasOptionsMenu(true)

        val (step, courseId) = getStepAndCourseIdArgs()
        val stepRepository: StepRepository by inject { parametersOf(courseId) }
        require(step.data is ShowDots)
        infoTextView.text = step.data.text?.parseAsHtml()
            ?: getString(R.string.lessons_show_dots_info_template)
                .format(step.data.dots.spelling)
        brailleDots.dotsState.display(step.data.dots)

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
