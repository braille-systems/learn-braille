package com.github.braillesystems.learnbraille.ui.screens.exit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.databinding.FragmentExitBinding
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragment
import com.github.braillesystems.learnbraille.utils.checkedAnnounce
import com.github.braillesystems.learnbraille.utils.navigate
import com.github.braillesystems.learnbraille.utils.navigateToLauncher
import com.github.braillesystems.learnbraille.utils.title

class ExitFragment : AbstractFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentExitBinding>(
        inflater,
        R.layout.fragment_exit,
        container,
        false
    ).ini().apply {

        title = getString(R.string.exit_question)
        checkedAnnounce(title)

        exitButton.setOnClickListener {
            navigate(R.id.action_global_menuFragment)
            navigateToLauncher()
        }

        continueButton.setOnClickListener {
            navigate(R.id.action_global_menuFragment)
        }

    }.root
}
