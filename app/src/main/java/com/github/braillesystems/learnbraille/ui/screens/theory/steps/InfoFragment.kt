package com.github.braillesystems.learnbraille.ui.screens.theory.steps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.Info
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsInfoBinding
import com.github.braillesystems.learnbraille.ui.screens.theory.getStepArg

class InfoFragment : AbstractInfoStepFragment(R.string.lessons_help_info) {

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
        initialize(step, prevButton, nextButton)
        updateTitle(getString(R.string.lessons_title_info))
        setText(step.data.text, infoTextView)
        setPrevButton(prevButton)
        setNextButton(nextButton)

    }.root
}
