package com.github.braillesystems.learnbraille.ui.screens.theory.steps.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.InputPhraseLetter
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsInputPhraseSymbolBinding
import com.github.braillesystems.learnbraille.res.inputSymbolPrintRules
import com.github.braillesystems.learnbraille.ui.screens.BrailleDotsInfo
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.StepBinding
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsViewMode
import com.github.braillesystems.learnbraille.utils.checkedAnnounce
import com.github.braillesystems.learnbraille.utils.getValue

class InputPhraseSymbolFragment : AbstractInputStepFragment(R.string.lessons_help_input_phrase) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsInputPhraseSymbolBinding>(
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
        require(stepData is InputPhraseLetter)
        val symbols: List<Symbol> = stepData.phrase.mapNotNull { it.data as? Symbol }
        val text: String = symbols.map { it.char }.joinToString(separator = "")

        val prevText = text.substring(0, stepData.pos)
        val currentSymbol = symbols[stepData.pos]
        val nextText = text.substring(stepData.pos + 1, symbols.size)

        prevLetters.text = prevText
        currentLetter.text = currentSymbol.char.toString()
        nextLetters.text = nextText
        letter.letter = currentSymbol.char

        val description = taskDescription(
            currentChar = currentSymbol.char,
            prevText = prevText,
            nextText = nextText
        )

        letter.contentDescription = description
        checkedAnnounce(description)

        inputViewModel = viewModel
        lifecycleOwner = this@InputPhraseSymbolFragment

    }.root

    private fun taskDescription(currentChar: Char, prevText: String, nextText: String): String {
        val fullText = prevText + currentChar + nextText
        var enterPrompt = getString(
            if (" " in fullText) R.string.input_phrase_template
            else R.string.input_word_template
        ).format(fullText)
        if (prevText.isNotEmpty()) enterPrompt += getString(R.string.input_phrase_complete_template)
            .format(prevText)
        return enterPrompt + inputSymbolPrintRules.getValue(currentChar)
    }
}
