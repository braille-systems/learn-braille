package ru.spbstu.amd.learnbraille.screens.lessons

import androidx.fragment.app.Fragment
import ru.spbstu.amd.learnbraille.database.entities.Step
import ru.spbstu.amd.learnbraille.database.entities.stepOf
import ru.spbstu.amd.learnbraille.screens.getStringArg
import ru.spbstu.amd.learnbraille.screens.lessons.BaseLessonFragment.Companion.stepArgName

open class BaseLessonFragment : Fragment() {
    companion object {
        const val stepArgName = "step"
    }
}

val BaseLessonFragment.stepArg: Step
    get() = stepOf(getStringArg(stepArgName))
