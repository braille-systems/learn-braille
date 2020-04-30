package com.github.braillesystems.learnbraille.ui.screens.lessons

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.db.getDBInstance
import com.github.braillesystems.learnbraille.data.types.FirstInfo
import com.github.braillesystems.learnbraille.databinding.FragmentLessonFirstInfoBinding
import com.github.braillesystems.learnbraille.defaultUser
import com.github.braillesystems.learnbraille.utils.updateTitle

class FirstInfoFragment : AbstractLesson(R.string.lessons_help_info) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonFirstInfoBinding>(
        inflater,
        R.layout.fragment_lesson_first_info,
        container,
        false
    ).apply {

        updateTitle(getString(R.string.lessons_title_info))
        setHasOptionsMenu(true)

        val step = getStepArg()
        require(step.data is FirstInfo)
        titleTextView.text = step.title
        infoTextView.text = step.data.text
        infoTextView.movementMethod = ScrollingMovementMethod()

        getDBInstance().apply {
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
