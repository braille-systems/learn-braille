package com.github.braillesystems.learnbraille.ui.screens

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.ui.screens.practice.PracticeFragmentDirections
import com.github.braillesystems.learnbraille.utils.announceByAccessibility
import com.github.braillesystems.learnbraille.utils.application
import timber.log.Timber

typealias HelpMsgId = Int

/**
 * Do not forget to add in onCreate `setHasOptionsMenu(true)`
 */
abstract class AbstractFragmentWithHelp(private val helpMsgId: HelpMsgId) : Fragment() {

    protected open val helpMsg: String
        get() = getString(helpMsgId)

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.help_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        super.onOptionsItemSelected(item).also {
            if (item.itemId == R.id.help) navigateToHelp()
        }

    protected fun navigateToHelp() {
        navigateToHelp(helpMsg)
    }

    private fun navigateToHelp(helpMsg: String) {
        Timber.i("Navigate to help")
        val action = PracticeFragmentDirections.actionGlobalHelpFragment()
        action.helpMessage = helpMsg
        application.announceByAccessibility(helpMsg)
        findNavController().navigate(action)
    }
}
