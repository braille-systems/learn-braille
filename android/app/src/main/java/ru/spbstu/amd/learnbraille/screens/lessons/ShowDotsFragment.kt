package ru.spbstu.amd.learnbraille.screens.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.entities.ShowDots
import ru.spbstu.amd.learnbraille.database.entities.spelling
import ru.spbstu.amd.learnbraille.database.getDBInstance
import ru.spbstu.amd.learnbraille.databinding.FragmentLessonsShowDotsBinding
import ru.spbstu.amd.learnbraille.defaultUser
import ru.spbstu.amd.learnbraille.screens.updateTitle
import ru.spbstu.amd.learnbraille.views.display
import ru.spbstu.amd.learnbraille.views.dots
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

        updateTitle(getString(R.string.lessons_help_show_dots))
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
