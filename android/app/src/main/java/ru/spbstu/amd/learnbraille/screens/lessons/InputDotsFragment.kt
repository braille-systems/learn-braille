package ru.spbstu.amd.learnbraille.screens.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.entities.InputDots
import ru.spbstu.amd.learnbraille.database.entities.spelling
import ru.spbstu.amd.learnbraille.database.getDBInstance
import ru.spbstu.amd.learnbraille.databinding.FragmentLessonsInputDotsBinding
import ru.spbstu.amd.learnbraille.defaultUser
import ru.spbstu.amd.learnbraille.screens.updateTitle
import ru.spbstu.amd.learnbraille.views.display
import ru.spbstu.amd.learnbraille.views.dots
import timber.log.Timber

class InputDotsFragment : AbstractLesson(R.string.lessons_help_input_dots) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsInputDotsBinding>(
        inflater,
        R.layout.fragment_lessons_input_dots,
        container,
        false
    ).apply {

        Timber.i("Start initialize input dots fragment")

        updateTitle(getString(R.string.lessons_title_input_dots))
        setHasOptionsMenu(true)

        val step = getStepArg()
        require(step.data is InputDots)
        titleTextView.text = step.title
        infoTextView.text = step.data.text
            ?: getString(R.string.lessons_input_dots_info_template)
                .format(step.data.dots.spelling)
        brailleDots.dots.display(step.data.dots)

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
        }

    }.root
}
