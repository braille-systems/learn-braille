package com.github.braillesystems.learnbraille.screens

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.screens.practice.PracticeFragmentDirections
import timber.log.Timber

typealias HelpMsgId = Int

/**
 * Do not forget to add in onCreate `setHasOptionsMenu(true)`
 */
abstract class AbstractFragmentWithHelp(val helpMsgId: HelpMsgId) : Fragment() {

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.help_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        super.onOptionsItemSelected(item).also {
            if (item.itemId == R.id.help) navigateToHelp()
        }

    protected open fun navigateToHelp(helpMsg:String){
        Timber.i("Navigate to help")
        val action = PracticeFragmentDirections.actionGlobalHelpFragment()
        action.helpMessage = helpMsg
        findNavController().navigate(action)
    }

    protected open fun navigateToHelp() {
        navigateToHelp(getString(helpMsgId))
    }
}
