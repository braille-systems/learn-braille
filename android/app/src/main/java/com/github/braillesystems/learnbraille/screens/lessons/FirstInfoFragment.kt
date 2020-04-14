package com.github.braillesystems.learnbraille.screens.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.database.entities.FirstInfo
import com.github.braillesystems.learnbraille.database.getDBInstance
import com.github.braillesystems.learnbraille.databinding.FragmentLessonFirstInfoBinding
import com.github.braillesystems.learnbraille.defaultUser
import com.github.braillesystems.learnbraille.util.updateTitle

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

        getDBInstance().apply {
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
