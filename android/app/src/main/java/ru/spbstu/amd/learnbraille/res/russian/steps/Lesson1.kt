package ru.spbstu.amd.learnbraille.res.russian.steps

import ru.spbstu.amd.learnbraille.database.*
import ru.spbstu.amd.learnbraille.database.BrailleDot.F
import ru.spbstu.amd.learnbraille.res.russian.symbols.symbolMap
import ru.spbstu.amd.learnbraille.res.stepFormat

/**
 * Automatically inserts proper lessonID.
 *
 * Cannot be replaced with factory returning lambda for each step because named parameters are
 * not supported for functional types in kotlin 1.3.
 */
private fun Step(title: String, data: StepData) =
    Step(
        title = title,
        lessonId = 1L,
        data = data
    )

// TODO fill steps
/**
 * List of steps for first lesson.
 *
 * Do not create symbols manually, always look them up in `symbolMap`.
 *
 * First two steps are used for database testing.
 */
val LESSON_1_STEPS = listOf(

    Step(
        title = "Знакомство с шеститочием",
        data = Info(
            """В рельефной азбуке Брайля любой символ - это шеститочие. 
                |Каждая точка из шести может быть выдавлена или пропущена. 
                |В следующем шаге все 6 точек выведены на экран."""
                .stepFormat()
        )
    ),

    Step(
        title = "Шеститочие",
        data = ShowDots(BrailleDots(F, F, F, F, F, F))
    ),

    Step(
        title = "Введите все шесть точек",
        data = InputDots(BrailleDots(F, F, F, F, F, F))
    ),

    Step(
        title = "Работа с букварём",
        data = Info(
            """Откройте букварь на странице 12. 
                |В верхней строке 14 раз повторён символ полного шеститочия."""
                .stepFormat()
        )
    ),

    Step(
        title = "Работа с букварём",
        data = Info(
            """Точки расположены в два столбца по три. 
                |Точки в первом столбце имеют номера 1, 2, 3 сверху вниз. 
                |Точки во втором столбце - 4, 5, 6 сверху вниз. 
                |Важно выучить, где какая точка."""
                .stepFormat()
        )
    ),

    // TODO replace
    Step(
        title = "Пример с символом",
        data = ShowSymbol(
            symbolMap['А'] ?: error("A russian not found")
        )
    )
)
