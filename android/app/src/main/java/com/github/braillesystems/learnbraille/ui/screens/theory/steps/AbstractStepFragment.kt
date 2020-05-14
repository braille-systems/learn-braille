package com.github.braillesystems.learnbraille.ui.screens.theory.steps

import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.ui.screens.HelpMsgId
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

    companion object {
        const val stepArgName = "step"
    }
}
