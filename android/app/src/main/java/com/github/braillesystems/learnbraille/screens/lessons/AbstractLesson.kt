package com.github.braillesystems.learnbraille.screens.lessons

import com.github.braillesystems.learnbraille.database.entities.stepOf
import com.github.braillesystems.learnbraille.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.screens.HelpMsgId
import com.github.braillesystems.learnbraille.screens.lessons.AbstractLesson.Companion.stepArgName
import com.github.braillesystems.learnbraille.util.getStringArg

abstract class AbstractLesson(helpMsgId: HelpMsgId) : AbstractFragmentWithHelp(helpMsgId) {
    companion object {
        const val stepArgName = "step"
    }
}

fun AbstractLesson.getStepArg() = stepOf(getStringArg(stepArgName))
