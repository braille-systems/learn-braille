package com.github.braillesystems.learnbraille.ui.screens.theory.steps.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsInfoBinding
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.StepBinding

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
    ).iniStep(
        titleId = R.string.lessons_title_info
    ) {
        object : StepBinding {
            override val prevButton: Button? = this@iniStep.prevButton
            override val nextButton: Button? = this@iniStep.nextButton
            override val textView: TextView? = this@iniStep.infoTextView
        }
    }.root
}
