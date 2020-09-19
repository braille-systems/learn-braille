package com.github.braillesystems.learnbraille.ui.screens.theory.steps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.MarkerSymbol
import com.github.braillesystems.learnbraille.data.entities.Show
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsShowMarkerBinding
import com.github.braillesystems.learnbraille.res.showMarkerPrintRules
import com.github.braillesystems.learnbraille.ui.screens.theory.getStepArg
import com.github.braillesystems.learnbraille.ui.views.display
import com.github.braillesystems.learnbraille.ui.views.dotsState
import com.github.braillesystems.learnbraille.utils.checkedAnnounce
import com.github.braillesystems.learnbraille.utils.contextNotNull
import com.github.braillesystems.learnbraille.utils.get
import timber.log.Timber

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
    ).also { binding ->

        Timber.i("Initialize show marker fragment")

        val step = getStepArg()
        require(step.data is Show)
        initialize(step, binding.prevButton, binding.nextButton)

        val data = step.data.material.data
        require(data is MarkerSymbol)
        val infoText = contextNotNull.showMarkerPrintRules[data.type]
            ?: error("No show print rules for marker: ${data.type}")
        setText(
            text = infoText,
            infoTextView = binding.infoTextView
        )
        checkedAnnounce(infoText)
        binding.brailleDots.dotsState.display(data.brailleDots)

        updateTitle(getString(R.string.lessons_title_show_symbol))
        setPrevButton(binding.prevButton)
        setNextButton(binding.nextButton)

    }.root
}
