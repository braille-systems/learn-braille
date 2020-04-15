package com.github.braillesystems.learnbraille.res.russian.steps

import com.github.braillesystems.learnbraille.database.entities.Step
import com.github.braillesystems.learnbraille.database.entities.StepData

private fun Step(title: String, data: StepData) =
    Step(
        title = title,
        lessonId = 2L,
        data = data
    )

val LESSON_2_STEPS get() = listOf<Step>()
