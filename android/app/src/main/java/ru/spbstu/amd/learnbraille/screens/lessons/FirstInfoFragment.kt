package ru.spbstu.amd.learnbraille.screens.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.lessons_buttonbar_right.view.*
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.entities.FirstInfo
import ru.spbstu.amd.learnbraille.database.getDBInstance
import ru.spbstu.amd.learnbraille.databinding.FragmentLessonFirstInfoBinding
import ru.spbstu.amd.learnbraille.defaultUser
import ru.spbstu.amd.learnbraille.screens.updateTitle

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
            infoButtonbarRight.next_button.setOnClickListener {
                navigateToNextStep(
                    stepDao, step, defaultUser,
                    userPassedStepDao
                )
            }
            infoButtonbarRight.to_curr_step_button.setOnClickListener {
                navigateToCurrentStep(
                    stepDao, defaultUser
                )
            }
        }

    }.root
}
