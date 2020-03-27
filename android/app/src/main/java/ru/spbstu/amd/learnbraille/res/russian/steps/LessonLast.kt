package ru.spbstu.amd.learnbraille.res.russian.steps

import ru.spbstu.amd.learnbraille.database.LastInfo
import ru.spbstu.amd.learnbraille.res.russian.PREPOPULATE_LESSONS

val VERY_LAST = listOf(
    ru.spbstu.amd.learnbraille.database.Step(
        title = "Последний шаг",
        lessonId = PREPOPULATE_LESSONS.last().id,
        data = LastInfo("""Это самый последний шаг! Теперь вы владеете азбукой Брайля!""")
    )
)
