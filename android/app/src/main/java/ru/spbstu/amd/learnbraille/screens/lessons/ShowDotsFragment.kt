package ru.spbstu.amd.learnbraille.screens.lessons


import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.databinding.FragmentLessonsDotsBinding
import ru.spbstu.amd.learnbraille.screens.updateTitle

class ShowDotsFragment : Fragment() {

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

    }.root

    // TODO support help
}
