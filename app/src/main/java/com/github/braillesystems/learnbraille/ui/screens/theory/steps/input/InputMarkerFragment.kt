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
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.StepBinding
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsView
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
    ).init(
        titleId = R.string.lessons_title_input_symbol,
        binding = {
            object : StepBinding {
                override val prevButton: Button? = this@init.prevButton
                override val nextButton: Button? = this@init.nextButton
                override val textView: TextView? = this@init.infoTextView
                override val brailleDots: BrailleDotsView? = this@init.brailleDots
            }
        }
    ).apply {

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
