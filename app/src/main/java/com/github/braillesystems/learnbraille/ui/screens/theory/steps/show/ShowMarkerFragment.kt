package com.github.braillesystems.learnbraille.ui.screens.theory.steps.show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.MarkerSymbol
import com.github.braillesystems.learnbraille.data.entities.Show
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsShowMarkerBinding
import com.github.braillesystems.learnbraille.res.showMarkerPrintRules
import com.github.braillesystems.learnbraille.ui.screens.BrailleDotsInfo
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.StepBinding
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsViewMode
import com.github.braillesystems.learnbraille.utils.checkedAnnounce
import com.github.braillesystems.learnbraille.utils.getValue

class ShowMarkerFragment : AbstractShowStepFragment(R.string.lessons_help_show_marker) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsShowMarkerBinding>(
        inflater,
        R.layout.fragment_lessons_show_marker,
        container,
        false
    ).iniStep(
        titleId = R.string.lessons_title_show_symbol
    ) {
        object : StepBinding {
            override val prevButton: Button? = this@iniStep.prevButton
            override val nextButton: Button? = this@iniStep.nextButton
            override val flipButton: Button? = this@iniStep.flipButton
            override val textView: TextView? = this@iniStep.infoTextView
            override val brailleDotsInfo: BrailleDotsInfo? = this@iniStep.run {
                BrailleDotsInfo(brailleDots, BrailleDotsViewMode.Reading, prevButton, flipButton)
            }
        }
    }.apply {

        val stepData = step.data
        require(stepData is Show)
        val data = stepData.material.data
        require(data is MarkerSymbol)

        val text = showMarkerPrintRules.getValue(data.type)
        infoTextView.text = text
        checkedAnnounce(text)

    }.root
}
