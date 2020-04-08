package ru.spbstu.amd.learnbraille.screens.lessons

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.LearnBrailleDatabase
import ru.spbstu.amd.learnbraille.database.entities.LastInfo
import ru.spbstu.amd.learnbraille.databinding.FragmentLessonLastInfoBinding
import ru.spbstu.amd.learnbraille.screens.updateTitle

class LastInfoFragment : BaseLessonFragment() {

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

        // TODO add content title

        val step = stepArg
        require(step.data is LastInfo)
        infoText.text = step.data.text

        val application: Application = requireNotNull(activity).application
        val database = LearnBrailleDatabase.getInstance(application)

        prevButton.setOnClickListener {
            navigateToPrevStep(database.stepDao, step)
        }

    }.root

    // TODO support help
}
