package com.github.braillesystems.learnbraille.ui.screens.theory.steps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.Show
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsShowSymbolBinding
import com.github.braillesystems.learnbraille.ui.PrintMode
import com.github.braillesystems.learnbraille.ui.printStringNotNullLogged
import com.github.braillesystems.learnbraille.ui.screens.theory.getStepArg
import com.github.braillesystems.learnbraille.ui.views.display
import com.github.braillesystems.learnbraille.ui.views.dotsState
import com.github.braillesystems.learnbraille.utils.checkedAnnounce
import timber.log.Timber

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
    ).apply {

        Timber.i("onCreateView")

        val step = getStepArg()
        require(step.data is Show)
        initialize(step, prevButton, nextButton)

        require(step.data.material.data is Symbol)
        letter.text = step.data.material.data.char.toString()
        brailleDots.dotsState.display(step.data.material.data.brailleDots)
        checkedAnnounce(printStringNotNullLogged(step.data.material.data.char, PrintMode.SHOW))

        updateTitle(getString(R.string.lessons_title_show_symbol))
        setPrevButton(prevButton)
        setNextButton(nextButton)

    }.root
}
