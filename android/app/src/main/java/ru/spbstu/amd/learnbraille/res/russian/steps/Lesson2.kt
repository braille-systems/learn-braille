package ru.spbstu.amd.learnbraille.res.russian.steps

import ru.spbstu.amd.learnbraille.database.Step
import ru.spbstu.amd.learnbraille.database.StepData

private fun Step(title: String, data: StepData) =
    Step(
        title = title,
        lessonId = 2L,
        data = data
    )

val LESSON_2_STEPS = listOf<Step>()
