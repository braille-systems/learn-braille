package ru.spbstu.amd.learnbraille.screens.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.entities.LastInfo
import ru.spbstu.amd.learnbraille.database.getDBInstance
import ru.spbstu.amd.learnbraille.databinding.FragmentLessonLastInfoBinding
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

        val step = stepArg
        require(step.data is LastInfo)
        infoText.text = step.data.text

        val database = getDBInstance()
        prevButton.setOnClickListener {
            navigateToPrevStep(database.stepDao, step)
        }

    }.root
}
