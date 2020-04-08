package ru.spbstu.amd.learnbraille.screens.lessons


import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.databinding.FragmentLessonsSymbolBinding
import ru.spbstu.amd.learnbraille.screens.updateTitle

class ShowSymbolFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsSymbolBinding>(
        inflater,
        R.layout.fragment_lessons_symbol,
        container,
        false
    ).apply {

        // TODO
        // TODO add content title

        updateTitle(getString(R.string.lessons_title_show_symbol))

    }.root

    // TODO support help
}
