package com.github.braillesystems.learnbraille.res.russian.steps

import com.github.braillesystems.learnbraille.database.entities.LastInfo
import com.github.braillesystems.learnbraille.database.entities.Step
import com.github.braillesystems.learnbraille.res.russian.PREPOPULATE_LESSONS

val VERY_LAST = Step(
    title = "Курс окончен",
    lessonId = PREPOPULATE_LESSONS.last().id,
    data = LastInfo(
        """Вы дошли до конца курса. Спасибо, что воспользовались нашим обучающим
            |приложением! Вы всегда можете вернутся к ранее пройденному материалу и повторить его.
        """
    )
)
