package ru.spbstu.amd.learnbraille.res.russian.steps

import ru.spbstu.amd.learnbraille.database.entities.*
import ru.spbstu.amd.learnbraille.database.entities.BrailleDot.E
import ru.spbstu.amd.learnbraille.database.entities.BrailleDot.F
import ru.spbstu.amd.learnbraille.res.russian.symbols.symbolMap

val DEBUG_LESSONS = listOf(

    Step(
        title = "0 First",
        lessonId = 1,
        data = FirstStep(
            text = "I am so first"
        )
    ),

    Step(
        title = "1 Info",
        lessonId = 1,
        data = Info(
            text = "I am best info step!"
        )
    ),

    Step(
        title = "2 Show dots, null text",
        lessonId = 1,
        data = ShowDots(
            text = null,
            dots = BrailleDots(F, F, E, E, F, F)
        )
    ),

    Step(
        title = "3 Show symbol",
        lessonId = 1,
        data = ShowSymbol(
            symbol = symbolMap['Ф'] ?: error("All RU letters are supported")
        )
    ),

    Step(
        title = "4 Input dots, some text :)",
        lessonId = 1,
        data = InputDots(
            text = "E E F F E E",
            dots = BrailleDots(E, E, F, F, E, E)
        )
    ),

    Step(
        title = "5, Input symbol",
        lessonId = 1,
        data = LastInfo(
            text = "The super last one"
        )
    )
)
