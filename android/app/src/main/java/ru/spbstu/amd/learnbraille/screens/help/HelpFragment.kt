package ru.spbstu.amd.learnbraille.screens.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.databinding.FragmentHelpBinding
import ru.spbstu.amd.learnbraille.screens.getStringArg
import ru.spbstu.amd.learnbraille.screens.updateTitle

class HelpFragment : Fragment() {

    private val helpMessageArgName = "help_message"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentHelpBinding>(
        inflater,
        R.layout.fragment_help,
        container,
        false
    ).apply {

        updateTitle(getString(R.string.help_title))
        helpMessage.text = getStringArg(helpMessageArgName)

    }.root
}
