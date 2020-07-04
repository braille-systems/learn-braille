package com.github.braillesystems.learnbraille.ui.screens.theory.steps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.FirstInfo
import com.github.braillesystems.learnbraille.databinding.FragmentLessonFirstInfoBinding
import com.github.braillesystems.learnbraille.ui.screens.theory.getStepArg

class FirstInfoFragment : AbstractInfoStepFragment(R.string.lessons_help_info) {

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
        initialize(step, null, nextButton)
        updateTitle(getString(R.string.lessons_title_info))
        setText(step.data.text, infoTextView)
        setNextButton(nextButton)

    }.root
}
