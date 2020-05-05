package com.github.braillesystems.learnbraille.ui.screens.theory

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.Info
import com.github.braillesystems.learnbraille.data.repository.StepRepository
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsInfoBinding
import com.github.braillesystems.learnbraille.ui.screens.getStepAndCourseIdArgs
import com.github.braillesystems.learnbraille.ui.screens.toCurrentStep
import com.github.braillesystems.learnbraille.ui.screens.toNextStep
import com.github.braillesystems.learnbraille.ui.screens.toPrevStep
import com.github.braillesystems.learnbraille.utils.updateTitle
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class InfoFragment : AbstractStepFragment(R.string.lessons_help_info) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsInfoBinding>(
        inflater,
        R.layout.fragment_lessons_info,
        container,
        false
    ).apply {

        updateTitle(getString(R.string.lessons_title_info))
        setHasOptionsMenu(true)

        val (step, courseId) = getStepAndCourseIdArgs()
        val stepRepository: StepRepository by inject { parametersOf(courseId) }
        require(step.data is Info)
        infoTextView.text = step.data.text
        infoTextView.movementMethod = ScrollingMovementMethod()

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
