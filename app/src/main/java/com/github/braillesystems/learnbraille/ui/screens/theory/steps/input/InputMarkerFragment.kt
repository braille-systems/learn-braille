package com.github.braillesystems.learnbraille.ui.screens.theory.steps.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.Input
import com.github.braillesystems.learnbraille.data.entities.MarkerSymbol
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsInputMarkerBinding
import com.github.braillesystems.learnbraille.res.inputMarkerPrintRules
import com.github.braillesystems.learnbraille.ui.screens.BrailleDotsInfo
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.StepBinding
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsViewMode
import com.github.braillesystems.learnbraille.utils.checkedAnnounce
import com.github.braillesystems.learnbraille.utils.getValue

class InputMarkerFragment : AbstractInputStepFragment(R.string.lessons_help_input_marker) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsInputMarkerBinding>(
        inflater,
        R.layout.fragment_lessons_input_marker,
        container,
        false
    ).iniStep(
        titleId = R.string.lessons_title_input_symbol
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

        val stepData = step.data
        require(stepData is Input)
        val data = stepData.material.data
        require(data is MarkerSymbol)
        val text = inputMarkerPrintRules.getValue(data.type)
        infoTextView.text = text
        checkedAnnounce(text)

        inputViewModel = viewModel
        lifecycleOwner = this@InputMarkerFragment

    }.root
}
