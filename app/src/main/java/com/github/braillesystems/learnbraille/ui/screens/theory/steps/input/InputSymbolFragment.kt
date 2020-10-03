package com.github.braillesystems.learnbraille.ui.screens.theory.steps.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.Input
import com.github.braillesystems.learnbraille.data.entities.StepData
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsInputSymbolBinding
import com.github.braillesystems.learnbraille.res.captionRules
import com.github.braillesystems.learnbraille.res.inputSymbolPrintRules
import com.github.braillesystems.learnbraille.ui.screens.BrailleDotsInfo
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.StepBinding
import com.github.braillesystems.learnbraille.ui.announceIncorrect
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsViewMode
import com.github.braillesystems.learnbraille.utils.checkedAnnounce
import com.github.braillesystems.learnbraille.utils.getValue

class InputSymbolFragment : AbstractInputStepFragment(R.string.lessons_help_input_symbol) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsInputSymbolBinding>(
        inflater,
        R.layout.fragment_lessons_input_symbol,
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
        require(stepData is Input)
        val data = stepData.material.data
        require(data is Symbol)
        letter.letter = data.char
        letterCaption.text = captionRules.getValue(data)
        checkedAnnounce(inputSymbolPrintRules.getValue(data.char))

        inputViewModel = viewModel
        lifecycleOwner = this@InputSymbolFragment

    }.root

    private fun StepData.char(): Char {
        require(this is Input)
        require(material.data is Symbol)
        return material.data.char
    }

    override fun announceIncorrect(data: StepData) {
        announceIncorrect(inputSymbolPrintRules.getValue(data.char()))
    }

    override fun onPassHint(data: StepData) {
        checkedAnnounce(inputSymbolPrintRules.getValue(data.char()))
    }
}
