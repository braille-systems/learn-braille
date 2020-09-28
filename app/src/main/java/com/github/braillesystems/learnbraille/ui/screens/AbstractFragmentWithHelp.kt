package com.github.braillesystems.learnbraille.ui.screens

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.ui.screens.help.HelpFragmentDirections
import com.github.braillesystems.learnbraille.utils.navigate
import timber.log.Timber

typealias HelpMsgId = Int

abstract class AbstractFragmentWithHelp(private val helpMsgId: HelpMsgId) : AbstractFragment() {

    override fun iniHelper() {
        setHasOptionsMenu(true)
    }

    protected open val helpMsg: String
        get() = getString(helpMsgId)

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.help_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        super
            .onOptionsItemSelected(item)
            .also { if (item.itemId == R.id.help) navigateToHelp() }

    protected fun navigateToHelp() {
        navigateToHelp(helpMsg)
    }

    private fun navigateToHelp(helpMsg: String) {
        Timber.i("Navigate to help")
        val action = HelpFragmentDirections.actionGlobalHelpFragment()
        action.helpMessage = helpMsg
        navigate(action)
    }
}
