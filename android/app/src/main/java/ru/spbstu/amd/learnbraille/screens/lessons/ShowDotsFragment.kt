package ru.spbstu.amd.learnbraille.screens.lessons

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.LearnBrailleDatabase
import ru.spbstu.amd.learnbraille.database.entities.ShowDots
import ru.spbstu.amd.learnbraille.databinding.FragmentLessonsDotsBinding
import ru.spbstu.amd.learnbraille.screens.updateTitle
import ru.spbstu.amd.learnbraille.views.display
import ru.spbstu.amd.learnbraille.views.dots

class ShowDotsFragment : BaseLessonFragment() {

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

        // TODO
        // TODO add content title

        updateTitle(getString(R.string.lessons_help_show_dots))

        val step = stepArg
        require(step.data is ShowDots)
        brailleDots.dots.display(step.data.dots)

        val application: Application = requireNotNull(activity).application
        val database = LearnBrailleDatabase.getInstance(application)

        prevButton.setOnClickListener {
            navigateToPrevStep(database.stepDao, step)
        }

    }.root

    // TODO support help
}
