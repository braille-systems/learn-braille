package com.github.braillesystems.learnbraille.ui.screens.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.types.ShowSymbol
import com.github.braillesystems.learnbraille.data.db.getDBInstance
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsShowSymbolBinding
import com.github.braillesystems.learnbraille.defaultUser
import com.github.braillesystems.learnbraille.utils.updateTitle
import com.github.braillesystems.learnbraille.ui.views.display
import com.github.braillesystems.learnbraille.ui.views.dotsState
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
        letter.text = step.data.symbol.symbol.toString()
        brailleDots.dotsState.display(step.data.symbol.brailleDots)

        getDBInstance().apply {
            prevButton.setOnClickListener {
                navigateToPrevStep(
                    current = step,
                    userId = defaultUser,
                    stepDao = stepDao,
                    lastStepDao = userLastStep
                )
            }
            nextButton.setOnClickListener {
                navigateToNextStep(
                    current = step,
                    userId = defaultUser,
                    stepDao = stepDao,
                    lastStepDao = userLastStep,
                    upsd = userPassedStepDao
                )
            }
            toCurrStepButton.setOnClickListener {
                navigateToCurrentStep(
                    userId = defaultUser,
                    stepDao = stepDao,
                    lastStepDao = userLastStep
                )
            }
        }

    }.root
}
