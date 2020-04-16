package com.github.braillesystems.learnbraille.res.russian

import com.github.braillesystems.learnbraille.database.entities.Lesson

// TODO fill lessons
val PREPOPULATE_LESSONS
    get() = listOf(
        Lesson(
            id = 1,
            name = "Знакомство с шеститочием"
        ),
        Lesson(
            id = 2,
            name = "Русские буквы А, Б, Ц"
        )
    )
