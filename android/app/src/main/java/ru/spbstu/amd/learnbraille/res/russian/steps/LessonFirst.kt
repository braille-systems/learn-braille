package ru.spbstu.amd.learnbraille.res.russian.steps

import ru.spbstu.amd.learnbraille.database.entities.FirstStep
import ru.spbstu.amd.learnbraille.database.entities.Step
import ru.spbstu.amd.learnbraille.res.russian.PREPOPULATE_LESSONS

val VERY_FIRST = Step(
    title = "Первый степ!",
    lessonId = PREPOPULATE_LESSONS.first().id,
    data = FirstStep("""Это ваш первый шаг на курсе, успехов!""")
)
