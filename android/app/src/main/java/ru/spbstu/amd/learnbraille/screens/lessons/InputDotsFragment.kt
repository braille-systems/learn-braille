package ru.spbstu.amd.learnbraille.screens.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.databinding.FragmentLessonsDotsBinding
import ru.spbstu.amd.learnbraille.screens.updateTitle

class InputDotsFragment : AbstractLesson(R.string.lessons_help_input_dots) {

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

        updateTitle(getString(R.string.lessons_title_input_dots))
        setHasOptionsMenu(true)

        // TODO
        // TODO add content title

    }.root
}
