package com.github.braillesystems.learnbraille.ui.screens.theory

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.LastInfo
import com.github.braillesystems.learnbraille.databinding.FragmentLessonLastInfoBinding
import com.github.braillesystems.learnbraille.utils.announceByAccessibility

class LastInfoFragment : AbstractStepFragment(R.string.lessons_help_last_info) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonLastInfoBinding>(
        inflater,
        R.layout.fragment_lesson_last_info,
        container,
        false
    ).apply {

        val step = getStepArg()
        require(step.data is LastInfo)
        infoTextView.text = step.data.text.parseAsHtml()
        infoTextView.movementMethod = ScrollingMovementMethod()
        this@LastInfoFragment.announceByAccessibility(step.data.text)

        updateStepTitle(step.lessonId, step.id, R.string.lessons_title_info)
        setHasOptionsMenu(true)

        prevButton.setOnClickListener {
            toPrevStep(step)
        }

    }.root
}
