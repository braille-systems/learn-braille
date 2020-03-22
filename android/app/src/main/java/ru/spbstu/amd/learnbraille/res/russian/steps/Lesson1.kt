package ru.spbstu.amd.learnbraille.res.russian.steps

import ru.spbstu.amd.learnbraille.database.Info
import ru.spbstu.amd.learnbraille.database.Input
import ru.spbstu.amd.learnbraille.database.Show
import ru.spbstu.amd.learnbraille.database.StepData
import ru.spbstu.amd.learnbraille.res.russian.symbols.FILLED_SYMBOL
import ru.spbstu.amd.learnbraille.res.russian.symbols.symbolMap

private fun Step(title: String, data: StepData) =
    ru.spbstu.amd.learnbraille.database.Step(
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
        Каждая точка из шести может быть выдавлена или пропущена.
        В следующем шаге все 6 точек выведены на экран.""".trim()
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
            "Откройте букварь на странице 12. " +
                    "В верхней строке 14 раз повторён символ полного шеститочия."
        )
    ),

    Step(
        title = "Работа с букварём",
        data = Info(
            "Точки расположены в два столбца по три. " +
                    "Точки в первом столбце имеют номера 1, 2, 3 сверху вниз. " +
                    "Точки во втором столбце - 4, 5, 6 сверху вниз. " +
                    "Важно выучить, где какая точка."
        )
    )
)
