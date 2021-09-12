package com.github.braillesystems.learnbraille.ui.screens.theory.steps.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsInputPhraseBinding
import com.github.braillesystems.learnbraille.ui.screens.BrailleDotsInfo
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.StepBinding
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsViewMode

class InputPhraseSymbolFragment : AbstractInputStepFragment(R.string.lessons_help_input_phrase) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsInputPhraseBinding>(
        inflater,
        R.layout.fragment_lessons_input_phrase_symbol,
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
        require(stepData is InputPhraseSymbol)


    }
}