package com.github.braillesystems.learnbraille.ui.screens.theory.steps.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.databinding.FragmentLessonFirstInfoBinding
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.StepBinding

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
    ).init(
        titleId = R.string.lessons_title_info,
        binding = {
            object : StepBinding {
                override val nextButton: Button? = this@init.nextButton
                override val textView: TextView? = this@init.infoTextView
            }
        }
    ).root
}
