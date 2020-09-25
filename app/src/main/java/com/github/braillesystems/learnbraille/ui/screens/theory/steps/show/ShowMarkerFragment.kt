package com.github.braillesystems.learnbraille.ui.screens.theory.steps.show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.text.parseAsHtml
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.MarkerSymbol
import com.github.braillesystems.learnbraille.data.entities.Show
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsShowMarkerBinding
import com.github.braillesystems.learnbraille.res.showMarkerPrintRules
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.AbstractStepFragment
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.StepBinding
import com.github.braillesystems.learnbraille.ui.views.display
import com.github.braillesystems.learnbraille.ui.views.dotsState
import com.github.braillesystems.learnbraille.utils.checkedAnnounce
import com.github.braillesystems.learnbraille.utils.getValue
import com.github.braillesystems.learnbraille.utils.removeHtmlMarkup

class ShowMarkerFragment : AbstractStepFragment(R.string.lessons_help_show_marker) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsShowMarkerBinding>(
        inflater,
        R.layout.fragment_lessons_show_marker,
        container,
        false
    ).init(
        titleId = R.string.lessons_title_show_symbol,
        binding = {
            object : StepBinding {
                override val prevButton: Button? = this@init.prevButton
                override val nextButton: Button? = this@init.nextButton
                override val textView: TextView? = this@init.infoTextView
            }
        }
    ).apply {

        val stepData = step.data
        require(stepData is Show)
        val data = stepData.material.data
        require(data is MarkerSymbol)

        val text = showMarkerPrintRules.getValue(data.type)
        infoTextView.text = text
        checkedAnnounce(text)
        brailleDots.dotsState.display(data.brailleDots)

    }.root
}
