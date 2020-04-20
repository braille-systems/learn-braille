package com.github.braillesystems.learnbraille.screens.lessons

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.database.entities.Info
import com.github.braillesystems.learnbraille.database.getDBInstance
import com.github.braillesystems.learnbraille.databinding.FragmentLessonsInfoBinding
import com.github.braillesystems.learnbraille.defaultUser
import com.github.braillesystems.learnbraille.util.updateTitle

class InfoFragment : AbstractLesson(R.string.lessons_help_info) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsInfoBinding>(
        inflater,
        R.layout.fragment_lessons_info,
        container,
        false
    ).apply {

        updateTitle(getString(R.string.lessons_title_info))
        setHasOptionsMenu(true)

        val step = getStepArg()
        require(step.data is Info)
        titleTextView.text = step.title
        infoTextView.text = step.data.text
        infoTextView.movementMethod= ScrollingMovementMethod()

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
