package ru.spbstu.amd.learnbraille.res.russian

import ru.spbstu.amd.learnbraille.database.entities.Lesson

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
