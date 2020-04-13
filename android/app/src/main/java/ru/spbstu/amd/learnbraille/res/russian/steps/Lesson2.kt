package ru.spbstu.amd.learnbraille.res.russian.steps

import ru.spbstu.amd.learnbraille.database.entities.Step
import ru.spbstu.amd.learnbraille.database.entities.StepData

private fun Step(title: String, data: StepData) =
    Step(
        title = title,
        lessonId = 2L,
        data = data
    )

val LESSON_2_STEPS get() = listOf<Step>()
