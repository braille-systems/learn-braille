package com.github.braillesystems.learnbraille.ui.screens.theory

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.FirstInfo
import com.github.braillesystems.learnbraille.data.repository.StepRepository
import com.github.braillesystems.learnbraille.databinding.FragmentLessonFirstInfoBinding
import com.github.braillesystems.learnbraille.ui.screens.getStepAndCourseIdArgs
import com.github.braillesystems.learnbraille.ui.screens.toCurrentStep
import com.github.braillesystems.learnbraille.ui.screens.toNextStep
import com.github.braillesystems.learnbraille.utils.updateTitle
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class FirstInfoFragment : AbstractStepFragment(R.string.lessons_help_info) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonFirstInfoBinding>(
        inflater,
        R.layout.fragment_lesson_first_info,
        container,
        false
    ).apply {

        updateTitle(getString(R.string.lessons_title_info))
        setHasOptionsMenu(true)

        val (step, courseId) = getStepAndCourseIdArgs()
        val stepRepository: StepRepository by inject { parametersOf(courseId) }
        require(step.data is FirstInfo)
        infoTextView.text = step.data.text.parseAsHtml()
        infoTextView.movementMethod = ScrollingMovementMethod()

        nextButton.setOnClickListener {
            toNextStep(step, stepRepository, true)
        }
        toCurrStepButton.setOnClickListener {
            toCurrentStep(stepRepository)
        }

    }.root
}
