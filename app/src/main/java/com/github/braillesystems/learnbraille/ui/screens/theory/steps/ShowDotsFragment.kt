package com.github.braillesystems.learnbraille.ui.screens.theory.steps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.ShowDots
import com.github.braillesystems.learnbraille.data.entities.spelling
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsShowDotsBinding
import com.github.braillesystems.learnbraille.ui.screens.theory.getStepArg
import com.github.braillesystems.learnbraille.ui.views.display
import com.github.braillesystems.learnbraille.ui.views.dotsState
import com.github.braillesystems.learnbraille.utils.checkedAnnounce
import timber.log.Timber

class ShowDotsFragment : AbstractStepFragment(R.string.lessons_help_show_dots) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsShowDotsBinding>(
        inflater,
        R.layout.fragment_lessons_show_dots,
        container,
        false
    ).apply {

        Timber.i("Initialize show dots fragment")

        val step = getStepArg()
        require(step.data is ShowDots)
        initialize(step, prevButton, nextButton)

        val infoText = step.data.text
            ?: getString(R.string.lessons_show_dots_info_template).format(step.data.dots.spelling)
        setText(
            text = infoText,
            infoTextView = infoTextView
        )
        checkedAnnounce(infoText)
        brailleDots.dotsState.display(step.data.dots)

        updateTitle(getString(R.string.lessons_title_show_dots))
        setPrevButton(prevButton)
        setNextButton(nextButton)

    }.root
}
