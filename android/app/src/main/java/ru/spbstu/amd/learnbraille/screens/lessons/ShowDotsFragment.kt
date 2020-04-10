package ru.spbstu.amd.learnbraille.screens.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.entities.ShowDots
import ru.spbstu.amd.learnbraille.database.entities.spelling
import ru.spbstu.amd.learnbraille.database.getDBInstance
import ru.spbstu.amd.learnbraille.databinding.FragmentLessonsDotsBinding
import ru.spbstu.amd.learnbraille.defaultUser
import ru.spbstu.amd.learnbraille.screens.updateTitle
import ru.spbstu.amd.learnbraille.views.clickable
import ru.spbstu.amd.learnbraille.views.display

class ShowDotsFragment : AbstractLesson(R.string.lessons_help_show_dots) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsDotsBinding>(
        inflater,
        R.layout.fragment_lessons_dots,
        container,
        false
    ).apply {

        updateTitle(getString(R.string.lessons_help_show_dots))
        setHasOptionsMenu(true)

        val step = getStepArg()
        require(step.data is ShowDots)
        infoText.text = getString(R.string.lessons_show_dots_info_template)
            .format(step.data.dots.spelling)
        brailleDots.dots.apply {
            display(step.data.dots)
            clickable(false)
        }

        val database = getDBInstance()
        prevButton.setOnClickListener {
            navigateToPrevStep(database.stepDao, step)
        }
        nextButton.setOnClickListener {
            navigateToNextStep(
                database.stepDao, step, defaultUser,
                database.userPassedStepDao
            )
        }

    }.root
}
