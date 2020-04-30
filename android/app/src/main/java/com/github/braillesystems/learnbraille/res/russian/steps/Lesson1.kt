package com.github.braillesystems.learnbraille.res.russian.steps

import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.F
import com.github.braillesystems.learnbraille.res.russian.PREPOPULATE_LESSONS

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

/**
 * List of steps for first lesson.
 *
 * Do not create symbols manually, always look them up in `symbolMap`.
 */
val LESSON_1_STEPS
    get() = listOf(

        Step(
            title = "Урок первый",
            data = Info(
                PREPOPULATE_LESSONS[0].name
            )
        ),

        Step(
            title = "Знакомство с шеститочием",
            data = Info(
                """В рельефной азбуке Брайля любой символ - это шеститочие. 
                |Каждая точка из шести может быть выдавлена или пропущена. 
                |В следующем шаге все 6 точек выведены на экран."""
            )
        ),

        Step(
            title = "Шеститочие",
            data = ShowDots(
                text = "Перед Вами полное шеститочие",
                dots = BrailleDots(
                    F,
                    F,
                    F,
                    F,
                    F,
                    F
                )
            )
        ),

        Step(
            title = "Полное шеститочие",
            data = InputDots(
                text = "Введите все шесть точек",
                dots = BrailleDots(
                    F,
                    F,
                    F,
                    F,
                    F,
                    F
                )
            )
        ),

        Step(
            title = "Работа с букварём",
            data = Info(
                """Откройте букварь на странице 12. 
                |В верхней строке 14 раз повторён символ полного шеститочия."""
            )
        ),

        Step(
            title = "Комментарий",
            data = Info(
                """Точки расположены в два столбца по три. 
                |Точки в первом столбце имеют номера 1, 2, 3 сверху вниз. 
                |Точки во втором столбце - 4, 5, 6 сверху вниз. 
                |Важно выучить, где какая точка."""
            )
        )
    )
