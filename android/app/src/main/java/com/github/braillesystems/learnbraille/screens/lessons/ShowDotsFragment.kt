package com.github.braillesystems.learnbraille.screens.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.database.entities.ShowDots
import com.github.braillesystems.learnbraille.database.entities.spelling
import com.github.braillesystems.learnbraille.database.getDBInstance
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsShowDotsBinding
import com.github.braillesystems.learnbraille.defaultUser
import com.github.braillesystems.learnbraille.util.updateTitle
import com.github.braillesystems.learnbraille.views.display
import com.github.braillesystems.learnbraille.views.dots
import timber.log.Timber

class ShowDotsFragment : AbstractLesson(R.string.lessons_help_show_dots) {

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

        updateTitle(getString(R.string.lessons_title_show_dots))
        setHasOptionsMenu(true)

        val step = getStepArg()
        require(step.data is ShowDots)
        titleTextView.text = step.title
        infoTextView.text = step.data.text
            ?: getString(R.string.lessons_show_dots_info_template)
                .format(step.data.dots.spelling)
        brailleDots.dots.display(step.data.dots)

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
