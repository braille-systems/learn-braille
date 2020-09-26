package com.github.braillesystems.learnbraille.ui.screens.theory.steps.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.databinding.FragmentLessonLastInfoBinding
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.StepBinding

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
    ).init(
        titleId = R.string.lessons_title_last_info,
        binding = {
            object : StepBinding {
                override val prevButton: Button? = this@init.prevButton
                override val textView: TextView? = this@init.infoTextView
            }
        }
    ).root
}
