package ru.spbstu.amd.learnbraille.screens.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.lessons_buttonbar_left.view.*
import kotlinx.android.synthetic.main.lessons_buttonbar_right.view.*
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.entities.Info
import ru.spbstu.amd.learnbraille.database.getDBInstance
import ru.spbstu.amd.learnbraille.databinding.FragmentLessonsInfoBinding
import ru.spbstu.amd.learnbraille.defaultUser
import ru.spbstu.amd.learnbraille.screens.updateTitle

class InfoFragment : AbstractLesson(R.string.lessons_help_info) {

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
        setHasOptionsMenu(true)

        val step = getStepArg()
        require(step.data is Info)
        titleTextView.text = step.title
        infoTextView.text = step.data.text

        getDBInstance().apply {
            infoButtonbarLeft.prev_button.setOnClickListener {
                navigateToPrevStep(stepDao, step)
            }
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
