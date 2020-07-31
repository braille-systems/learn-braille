package com.github.braillesystems.learnbraille.ui.screens.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.databinding.FragmentHelpBinding
import com.github.braillesystems.learnbraille.utils.checkedAnnounce
import com.github.braillesystems.learnbraille.utils.getFragmentStringArg
import com.github.braillesystems.learnbraille.utils.removeHtmlMarkup
import com.github.braillesystems.learnbraille.utils.title

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

        title = getString(R.string.help_title)

        val content = getFragmentStringArg(helpMessageArgName)
        helpView.setSeparatedText(content)
        checkedAnnounce(content.removeHtmlMarkup())

    }.root
}
