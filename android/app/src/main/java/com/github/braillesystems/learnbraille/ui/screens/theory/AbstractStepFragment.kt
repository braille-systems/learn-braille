package com.github.braillesystems.learnbraille.ui.screens.theory

import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.ui.screens.HelpMsgId


/**
 * Base class for all lessons.
 */
abstract class AbstractStepFragment(helpMsgId: HelpMsgId) : AbstractFragmentWithHelp(helpMsgId) {

    override val helpMsg: String
        get() = super.helpMsg + getString(R.string.lessons_help_common)

    companion object {
        const val stepArgName = "step"
    }
}
