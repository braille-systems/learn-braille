package com.github.braillesystems.learnbraille.res.russian.steps

import com.github.braillesystems.learnbraille.data.types.FirstInfo
import com.github.braillesystems.learnbraille.data.types.Step
import com.github.braillesystems.learnbraille.res.russian.PREPOPULATE_LESSONS

val VERY_FIRST = Step(
    title = "О курсе",
    lessonId = PREPOPULATE_LESSONS.first().id,
    data = FirstInfo(
        """Перед Вами пошаговый курс для обучения с нуля системе Луи Брайля.
        |Курс построен на основе книги В. В. Голубиной "Пособие по изучению системы Л. Брайля".
        |В течение курса Вы ознакомитесь с обозначениями букв, цифр и специальных символов.
        |В некоторых шагах предлагается выполнять задания, используя книгу Голубиной.
        |Если Вам будет непонятно, как пользоваться курсом, Вы в любой момент можете вызвать справку
        |по разделу, нажав кнопку "справка" в правом верхнем углу экрана.
        """
    )
)
