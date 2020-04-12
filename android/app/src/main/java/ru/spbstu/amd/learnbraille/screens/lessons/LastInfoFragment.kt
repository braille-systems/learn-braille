package ru.spbstu.amd.learnbraille.screens.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.entities.LastInfo
import ru.spbstu.amd.learnbraille.database.getDBInstance
import ru.spbstu.amd.learnbraille.databinding.FragmentLessonLastInfoBinding
import ru.spbstu.amd.learnbraille.defaultUser
import ru.spbstu.amd.learnbraille.screens.updateTitle

class LastInfoFragment : AbstractLesson(R.string.lessons_help_last_info) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonLastInfoBinding>(
        inflater,
        R.layout.fragment_lesson_last_info,
        container,
        false
    ).apply {

        updateTitle(getString(R.string.lessons_title_last_info))
        setHasOptionsMenu(true)

        val step = getStepArg()
        require(step.data is LastInfo)
        titleTextView.text = step.title
        infoTextView.text = step.data.text

        getDBInstance().run {
            prevButton.setOnClickListener {
                navigateToPrevStep(stepDao, step)
            }
            toCurrStepButton.setOnClickListener {
                navigateToCurrentStep(
                    stepDao, defaultUser
                )
            }
        }

    }.root
}
