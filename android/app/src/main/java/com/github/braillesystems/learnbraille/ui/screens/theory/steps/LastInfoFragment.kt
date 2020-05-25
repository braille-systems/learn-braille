package com.github.braillesystems.learnbraille.ui.screens.theory.steps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.LastInfo
import com.github.braillesystems.learnbraille.databinding.FragmentLessonLastInfoBinding
import com.github.braillesystems.learnbraille.ui.screens.theory.getStepArg

class LastInfoFragment : AbstractInfoStepFragment(R.string.lessons_help_last_info) {

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
        initialize(step, prevButton, null)
        updateTitle(getString(R.string.lessons_title_last_info))
        setText(step.data.text, infoTextView)
        setPrevButton(prevButton)

    }.root
}
