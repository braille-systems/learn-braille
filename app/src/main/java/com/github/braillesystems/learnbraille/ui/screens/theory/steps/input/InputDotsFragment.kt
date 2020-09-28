package com.github.braillesystems.learnbraille.ui.screens.theory.steps.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.text.parseAsHtml
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.InputDots
import com.github.braillesystems.learnbraille.data.entities.spelling
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsInputDotsBinding
import com.github.braillesystems.learnbraille.ui.screens.BrailleDotsInfo
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.StepBinding
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsViewMode
import com.github.braillesystems.learnbraille.utils.checkedAnnounce
import com.github.braillesystems.learnbraille.utils.removeHtmlMarkup

class InputDotsFragment : AbstractInputStepFragment(R.string.lessons_help_input_dots) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsInputDotsBinding>(
        inflater,
        R.layout.fragment_lessons_input_dots,
        container,
        false
    ).iniStep(
        titleId = R.string.lessons_title_input_dots
    ) {
        object : StepBinding {
            override val prevButton: Button? = this@iniStep.prevButton
            override val nextButton: Button? = this@iniStep.nextButton
            override val flipButton: Button? = this@iniStep.flipButton
            override val hintButton: Button? = this@iniStep.hintButton
            override val textView: TextView? = this@iniStep.infoTextView
            override val brailleDotsInfo: BrailleDotsInfo? = this@iniStep.run {
                BrailleDotsInfo(
                    brailleDots,
                    if (preferenceRepository.isWriteModeFirst) BrailleDotsViewMode.Writing
                    else BrailleDotsViewMode.Reading,
                    prevButton, flipButton
                )
            }
        }
    }.apply {

        val data = step.data
        require(data is InputDots)
        val text = data.text
            ?: getString(R.string.lessons_show_dots_info_template).format(data.brailleDots.spelling)
        infoTextView.text = text.parseAsHtml()
        checkedAnnounce(text.removeHtmlMarkup())

        inputViewModel = viewModel
        lifecycleOwner = this@InputDotsFragment

    }.root
}
