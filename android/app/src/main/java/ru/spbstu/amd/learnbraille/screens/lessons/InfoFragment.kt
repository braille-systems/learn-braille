package ru.spbstu.amd.learnbraille.screens.lessons


import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.LearnBrailleDatabase
import ru.spbstu.amd.learnbraille.database.entities.Info
import ru.spbstu.amd.learnbraille.databinding.FragmentLessonsInfoBinding
import ru.spbstu.amd.learnbraille.defaultUser
import ru.spbstu.amd.learnbraille.screens.updateTitle

class InfoFragment : BaseLessonFragment() {

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

        val step = stepArg
        require(step.data is Info)
        infoText.text = step.data.text
        titleView.text = step.title

        val application: Application = requireNotNull(activity).application
        val database = LearnBrailleDatabase.getInstance(application)

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

    // TODO support help
}
