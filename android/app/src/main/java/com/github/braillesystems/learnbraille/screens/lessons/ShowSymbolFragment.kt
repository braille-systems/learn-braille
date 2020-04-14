package com.github.braillesystems.learnbraille.screens.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.database.entities.ShowSymbol
import com.github.braillesystems.learnbraille.database.getDBInstance
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsShowSymbolBinding
import com.github.braillesystems.learnbraille.defaultUser
import com.github.braillesystems.learnbraille.util.updateTitle
import com.github.braillesystems.learnbraille.views.display
import com.github.braillesystems.learnbraille.views.dots
import timber.log.Timber

class ShowSymbolFragment : AbstractLesson(R.string.lessons_help_show_symbol) {

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

        Timber.i("Initialize show symbol fragment")

        updateTitle(getString(R.string.lessons_title_show_symbol))
        setHasOptionsMenu(true)

        val step = getStepArg()
        require(step.data is ShowSymbol)
        titleTextView.text = step.title
        infoTextView.text = step.data.symbol.symbol.toString()
        brailleDots.dots.display(step.data.symbol.brailleDots)

        getDBInstance().apply {
            prevButton.setOnClickListener {
                navigateToPrevStep(stepDao, step)
            }
            nextButton.setOnClickListener {
                navigateToNextStep(
                    stepDao, step, defaultUser,
                    userPassedStepDao
                )
            }
            toCurrStepButton.setOnClickListener {
                navigateToCurrentStep(
                    stepDao, defaultUser
                )
            }
        }

    }.root
}
