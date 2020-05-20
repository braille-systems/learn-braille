package com.github.braillesystems.learnbraille.ui.screens.theory.steps

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.github.braillesystems.learnbraille.COURSE_ID
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.ui.screens.HelpMsgId
import com.github.braillesystems.learnbraille.ui.screens.theory.toCurrentStep
import com.github.braillesystems.learnbraille.utils.navigate
import com.github.braillesystems.learnbraille.utils.updateTitle


/**
 * Base class for all lessons.
 */
abstract class AbstractStepFragment(helpMsgId: HelpMsgId) : AbstractFragmentWithHelp(helpMsgId) {

    override val helpMsg: String
        get() = super.helpMsg + getString(R.string.lessons_help_common)

    protected fun updateStepTitle(lessonId: Long, stepId: Long, msgId: Int) {
        updateTitle("$lessonId.$stepId ${getString(msgId)}")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.steps_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = false.also {
        when (item.itemId) {
            R.id.help -> navigateToHelp()
            R.id.lessons_list -> navigate(R.id.action_global_lessonsListFragment)
            R.id.current_course_pos -> toCurrentStep(COURSE_ID)
        }
    }

    companion object {
        const val stepArgName = "step"
    }
}
