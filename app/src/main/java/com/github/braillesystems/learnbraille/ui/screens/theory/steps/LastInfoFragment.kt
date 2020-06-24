package com.github.braillesystems.learnbraille.ui.screens.theory.steps

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.LastInfo
import com.github.braillesystems.learnbraille.databinding.FragmentLessonLastInfoBinding
import com.github.braillesystems.learnbraille.ui.screens.theory.getStepArg
import com.github.braillesystems.learnbraille.ui.screens.theory.toPrevStep
import com.github.braillesystems.learnbraille.utils.checkedAnnounce

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
        checkedAnnounce(step.data.text)

        updateStepTitle(step.lessonId, step.id, R.string.lessons_title_info)
        setHasOptionsMenu(true)

        prevButton.setOnClickListener {
            toPrevStep(step)
        }

    }.root
}
