package com.github.braillesystems.learnbraille.res.russian.steps

import com.github.braillesystems.learnbraille.database.entities.LastInfo
import com.github.braillesystems.learnbraille.database.entities.Step
import com.github.braillesystems.learnbraille.res.russian.PREPOPULATE_LESSONS

val VERY_LAST = Step(
    title = "Последний шаг",
    lessonId = PREPOPULATE_LESSONS.last().id,
    data = LastInfo("""Это самый последний шаг! Теперь вы владеете азбукой Брайля!""")
)
