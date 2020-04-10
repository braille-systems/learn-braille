package ru.spbstu.amd.learnbraille.screens.lessons

import ru.spbstu.amd.learnbraille.database.entities.Step
import ru.spbstu.amd.learnbraille.database.entities.stepOf
import ru.spbstu.amd.learnbraille.screens.AbstractFragmentWithHelp
import ru.spbstu.amd.learnbraille.screens.HelpMsgId
import ru.spbstu.amd.learnbraille.screens.getStringArg
import ru.spbstu.amd.learnbraille.screens.lessons.AbstractLesson.Companion.stepArgName

abstract class AbstractLesson(helpMsgId: HelpMsgId) : AbstractFragmentWithHelp(helpMsgId) {
    companion object {
        const val stepArgName = "step"
    }
}

val AbstractLesson.stepArg: Step
    get() = stepOf(getStringArg(stepArgName))
