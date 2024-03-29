package com.github.braillesystems.learnbraille.res

import com.github.braillesystems.learnbraille.data.dsl.annotate
import com.github.braillesystems.learnbraille.data.dsl.lessons
import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.F

internal val testLessons by lessons {

    val name1 = "Знакомство с шеститочием"
    lesson(
        name = name1
    ) {
        +FirstInfo(
            """Перед Вами пошаговый курс для обучения с нуля системе Луи Брайля.
                <br><br>
                Курс построен на основе книги В. В. Голубиной "Пособие по изучению системы Л. Брайля".
                В течение курса Вы ознакомитесь с обозначениями букв, цифр и специальных символов.
                В некоторых шагах предлагается выполнять задания, используя книгу Голубиной.
                Если Вам будет непонятно, как пользоваться курсом, Вы в любой момент можете вызвать справку
                по разделу, нажав кнопку "справка" в правом верхнем углу экрана.
            """
        )
        +Info(name1)
        +Info(
            """В рельефной системе Брайля любой символ - это шеститочие. 
                Каждая точка из шести может быть выдавлена или пропущена. 
                В следующем шаге все 6 точек выведены на экран."""
        )
        +ShowDots(
            text = "Перед Вами полное шеститочие",
            brailleDots = BrailleDots(F, F, F, F, F, F)
        )
        +InputDots(
            text = "Введите все шесть точек",
            brailleDots = BrailleDots(F, F, F, F, F, F)
        )
        +Info(
            """Откройте пособие на странице 12. 
                В верхней строке 14 раз повторён символ полного шеститочия."""
        ).annotate(StepAnnotation.golubinaBookRequired)
        +Info(
            """Точки расположены в два столбца по три. 
                Точки в первом столбце имеют номера 1, 2, 3 сверху вниз. 
                Точки во втором столбце - 4, 5, 6 сверху вниз. 
                Важно выучить, где какая точка."""
        )
    }

    lesson(
        name = "Русские буквы А, Б, Ц"
    ) {
        +Info(
            """Некоторые символы Брайля обозначают как букву, так и цифру. Это буквы А, Б,
                    Ц и так далее. С добавлением цифрового знака, который мы изучим в следующем
                    уроке, из них получаются цифры 1, 2, 3 и так далее."""
        )
        +Info(
            """Буква А обозначается одной точкой, точкой номер один.
                    Ознакомьтесь с ней."""
        )
        +Show(content.symbols.getValue('А'))
        +Info(
            """Откройте пособие на странице 13. Вверху слева рельефно-графическое
                    изображение буквы А. Рядом после полного шеститочия пять раз повторена 
                    буква А точечным шрифтом."""
        ).annotate(StepAnnotation.golubinaBookRequired)
        +Input(content.symbols.getValue('А'))

        // Testing marker symbols representation
        val greek = content.markers.getValue(MarkerType.GreekCapital)
        +Show(greek)
        +Input(greek)
        +Show(content.markers.getValue(MarkerType.LatinCapital))
        +Show(content.markers.getValue(MarkerType.RussianCapital))

        +Show(content.symbols.getValue('Б'))
        +Info(
            """Снова изучим страницу 13 в пособии. Под строкой с буквой А - 
                    такая же с буквой Б."""
        ).annotate(StepAnnotation.golubinaBookRequired)
        +Input(content.symbols.getValue('Б'))
        +Info(
            """Ознакомьтесь с буквой Ц на странице 13 пособия. 
                    Строка с буквой Ц находится под строкой с буквой Б."""
        ).annotate(StepAnnotation.golubinaBookRequired)
        +Show(content.symbols.getValue('Ц'))
        +Input(content.symbols.getValue('Ц'))

        +Info("""Symbols of other types for testing""")
        +Input(content.symbols.getValue(','))
        +Input(content.symbols.getValue('1'))
        +Input(content.symbols.getValue('2'))
        +Input(content.symbols.getValue('3'))

        +Info(
            """Поздравляем! Второй урок пройден. В следующем занятии мы узнаем, 
                    как с помощью букв А, Б и Ц составить цифры 1, 2 и 3."""
        )
        +LastInfo(
            """Вы дошли до конца курса. Спасибо, что воспользовались нашим обучающим
            приложением! Вы всегда можете вернутся к ранее пройденному материалу и повторить его.
            Продолжение следует!
            """
        )
    }
}
