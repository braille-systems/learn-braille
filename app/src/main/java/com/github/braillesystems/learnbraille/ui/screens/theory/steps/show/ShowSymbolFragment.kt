package com.github.braillesystems.learnbraille.ui.screens.theory.steps.show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.Show
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsShowSymbolBinding
import com.github.braillesystems.learnbraille.res.showSymbolPrintRules
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.AbstractStepFragment
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.StepBinding
import com.github.braillesystems.learnbraille.ui.views.display
import com.github.braillesystems.learnbraille.ui.views.dotsState
import com.github.braillesystems.learnbraille.utils.checkedAnnounce
import com.github.braillesystems.learnbraille.utils.getValue

class ShowSymbolFragment : AbstractStepFragment(R.string.lessons_help_show_symbol) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsShowSymbolBinding>(
        inflater,
        R.layout.fragment_lessons_show_symbol,
        container,
        false
    ).init(
        titleId = R.string.lessons_title_show_symbol,
        binding = {
            object : StepBinding {
                override val prevButton: Button? = this@init.prevButton
                override val nextButton: Button? = this@init.nextButton
            }
        }
    ).apply {

        val stepData = step.data
        require(stepData is Show)

        val data = stepData.material.data
        require(data is Symbol)
        letter.letter = data.char
        checkedAnnounce(showSymbolPrintRules.getValue(data.char))
        brailleDots.dotsState.display(data.brailleDots)

    }.root
}
