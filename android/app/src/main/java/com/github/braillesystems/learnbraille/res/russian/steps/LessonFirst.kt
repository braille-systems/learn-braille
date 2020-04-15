package com.github.braillesystems.learnbraille.res.russian.steps

import com.github.braillesystems.learnbraille.database.entities.FirstInfo
import com.github.braillesystems.learnbraille.database.entities.Step
import com.github.braillesystems.learnbraille.res.russian.PREPOPULATE_LESSONS

val VERY_FIRST = Step(
    title = "Первый степ!",
    lessonId = PREPOPULATE_LESSONS.first().id,
    data = FirstInfo("""Это ваш первый шаг на курсе, успехов!""")
)
