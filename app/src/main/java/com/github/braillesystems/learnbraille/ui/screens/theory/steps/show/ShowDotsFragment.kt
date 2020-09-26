package com.github.braillesystems.learnbraille.ui.screens.theory.steps.show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.ShowDots
import com.github.braillesystems.learnbraille.data.entities.spelling
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsShowDotsBinding
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.AbstractStepFragment
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.StepBinding
import com.github.braillesystems.learnbraille.ui.views.display
import com.github.braillesystems.learnbraille.ui.views.dotsState
import com.github.braillesystems.learnbraille.utils.checkedAnnounce

class ShowDotsFragment : AbstractStepFragment(R.string.lessons_help_show_dots) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsShowDotsBinding>(
        inflater,
        R.layout.fragment_lessons_show_dots,
        container,
        false
    ).init(
        titleId = R.string.lessons_title_show_dots,
        binding = {
            object : StepBinding {
                override val prevButton: Button? = this@init.prevButton
                override val nextButton: Button? = this@init.nextButton
                override val textView: TextView? = this@init.infoTextView
            }
        }
    ).apply {

        val data = step.data
        require(data is ShowDots)

        val text = data.text
            ?: getString(R.string.lessons_show_dots_info_template).format(data.brailleDots.spelling)
        infoTextView.text = text
        checkedAnnounce(text)
        brailleDots.dotsState.display(data.brailleDots)

    }.root
}
