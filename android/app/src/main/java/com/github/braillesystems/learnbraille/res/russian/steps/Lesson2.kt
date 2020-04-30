package com.github.braillesystems.learnbraille.res.russian.steps

import com.github.braillesystems.learnbraille.data.entities.Info
import com.github.braillesystems.learnbraille.data.entities.InputSymbol
import com.github.braillesystems.learnbraille.data.entities.ShowSymbol
import com.github.braillesystems.learnbraille.data.entities.StepData
import com.github.braillesystems.learnbraille.res.russian.symbols.symbolMap

private fun Step(title: String, data: StepData) =
    com.github.braillesystems.learnbraille.data.entities.Step(
        title = title,
        lessonId = 2L,
        data = data
    )


val LESSON_2_STEPS
    get() = listOf(

        Step(
            title = "Урок 2. Вступление",
            data = Info(
                """Некоторые символы Брайля обозначают как букву, так и цифру. Это буквы А, Б,
                    |Ц и так далее. С добавлением цифрового знака, который мы изучим в следующем
                    |уроке, из них получаются цифры 1, 2, 3 и так далее."""
            )
        ),

        Step(
            title = "Буква А. Комментарий",
            data = Info(
                """Буква А обозначается одной точкой, точкой номер один.
                    |Ознакомьтесь с ней."""
            )
        ),

        Step(
            title = "Точечный состав буквы А.",
            data = ShowSymbol(
                symbolMap['А'] ?: error("A russian not found")
            )
        ),

        Step(
            title = "Работа с букварём",
            data = Info(
                """Откройте букварь на странице 13. Вверху слева рельефно-графическое
                    |изображение буквы А. Рядом после полного шеститочия пять раз повторена 
                    |буква А точечным шрифтом."""
            )
        ),

        Step(
            title = "Введите букву А.",
            data = InputSymbol(
                symbolMap['А'] ?: error("A russian not found")
            )
        ),

        Step(
            title = "Точечный состав буквы Б.",
            data = ShowSymbol(
                symbolMap['Б'] ?: error("Б russian not found")
            )
        ),

        Step(
            title = "Работа с букварём",
            data = Info(
                """Снова изучим страницу 13 в букваре. Под строкой с буквой А - 
                    |такая же с буквой Б."""
            )
        ),

        Step(
            title = "Введите букву Б.",
            data = InputSymbol(
                symbolMap['Б'] ?: error("Б russian not found")
            )
        ),

        Step(
            title = "Буква Ц. Точечный состав.",
            data = ShowSymbol(
                symbolMap['Ц'] ?: error("Ц russian not found")
            )
        ),

        Step(
            title = "Работа с букварём",
            data = Info(
                """Ознакомьтесь с буквой Ц на странице 13 букваря. 
                    |Строка с буквой Ц находится под строкой с буквой Б."""
            )
        ),

        Step(
            title = "Введите букву Ц.",
            data = InputSymbol(
                symbolMap['Ц'] ?: error("Ц russian not found")
            )
        ),

        Step(
            title = "В завершение второго урока",
            data = Info(
                """Поздравляем! Второй урок пройден. В следующем занятии мы узнаем, 
                    |как с помощью букв А, Б и Ц составить цифры 1, 2 и 3."""
            )
        )
    )
