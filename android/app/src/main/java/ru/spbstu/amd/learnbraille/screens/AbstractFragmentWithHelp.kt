package ru.spbstu.amd.learnbraille.screens

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.screens.practice.PracticeFragmentDirections
import timber.log.Timber

typealias HelpMsgId = Int

/**
 * Do not forget to add in onCreate `setHasOptionsMenu(true)`
 */
abstract class AbstractFragmentWithHelp(private val helpMsgId: HelpMsgId) : Fragment() {

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.help_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        super.onOptionsItemSelected(item).also {
            if (item.itemId == R.id.help) navigateToHelp()
        }

    protected fun navigateToHelp() {
        Timber.i("Navigate to help")
        val action = PracticeFragmentDirections.actionGlobalHelpFragment()
        action.helpMessage = getString(helpMsgId)
        findNavController().navigate(action)
    }
}
