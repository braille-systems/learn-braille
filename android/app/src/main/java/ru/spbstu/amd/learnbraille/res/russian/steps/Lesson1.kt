package ru.spbstu.amd.learnbraille.res.russian.steps

import ru.spbstu.amd.learnbraille.database.*
import ru.spbstu.amd.learnbraille.database.BrailleDot.F
import ru.spbstu.amd.learnbraille.res.russian.symbols.FILLED_SYMBOL
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
        data = Show(
            symbolMap[FILLED_SYMBOL] ?: error("Filled symbol not found")
        )
    ),

    Step(
        title = "Введите все шесть точек",
        data = Input(
            symbolMap[FILLED_SYMBOL] ?: error("Filled symbol not found")
        )
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

    Step(
        title = "Пример с кастомным шеститочием",
        data = Show(
            Symbol(
                symbol = ' ',
                language = Language.NONE,
                brailleDots = BrailleDots(
                    b1 = F,
                    b3 = F,
                    b4 = F
                )
            )
        )
    )
)
