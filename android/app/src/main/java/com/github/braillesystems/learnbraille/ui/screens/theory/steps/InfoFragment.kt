package com.github.braillesystems.learnbraille.ui.screens.theory.steps

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.Info
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsInfoBinding
import com.github.braillesystems.learnbraille.ui.screens.theory.getStepArg
import com.github.braillesystems.learnbraille.ui.screens.theory.toCurrentStep
import com.github.braillesystems.learnbraille.ui.screens.theory.toNextStep
import com.github.braillesystems.learnbraille.ui.screens.theory.toPrevStep

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

        val step = getStepArg()
        require(step.data is Info)
        infoTextView.text = step.data.text.parseAsHtml()
        infoTextView.movementMethod = ScrollingMovementMethod()

        updateStepTitle(step.lessonId, step.id, R.string.lessons_title_info)
        setHasOptionsMenu(true)

        prevButton.setOnClickListener {
            toPrevStep(step)
        }
        nextButton.setOnClickListener {
            toNextStep(step, markThisAsPassed = true)
        }
        toCurrStepButton.setOnClickListener {
            toCurrentStep(step.courseId)
        }

    }.root
}
