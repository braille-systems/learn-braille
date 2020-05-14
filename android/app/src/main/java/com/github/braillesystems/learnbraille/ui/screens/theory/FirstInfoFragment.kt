package com.github.braillesystems.learnbraille.ui.screens.theory

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.FirstInfo
import com.github.braillesystems.learnbraille.databinding.FragmentLessonFirstInfoBinding

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

        val step = getStepArg()
        require(step.data is FirstInfo)
        infoTextView.text = step.data.text.parseAsHtml()
        infoTextView.movementMethod = ScrollingMovementMethod()

        updateStepTitle(step.lessonId, step.id, R.string.lessons_title_info)
        setHasOptionsMenu(true)

        nextButton.setOnClickListener {
            toNextStep(step, markThisAsPassed = true)
        }
        toCurrStepButton.setOnClickListener {
            toCurrentStep(step.courseId)
        }

    }.root
}
