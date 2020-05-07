package com.github.braillesystems.learnbraille.res

import com.github.braillesystems.learnbraille.data.dsl.annotate
import com.github.braillesystems.learnbraille.data.dsl.data
import com.github.braillesystems.learnbraille.data.dsl.lessons
import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.F
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.E


object SymbolType {
    const val ru = "ru"
    const val special = "special"
    const val digit = "digit"
}

object StepAnnotation {
    const val someBookRequired = "some_book_required"
}


/**
 * Do not change name of this property, it is used for prepopulation.
 *
 * Use `DslTest.kt` file as DSL tutorial.
 *
 * Text in steps is parsed as HTML.
 */
val prepopulationData by data(
    materials = practiceContent
) {

    users {
        user(
            login = "default",
            name = "John Smith"
        )
    }

    courses {
        course(
            name = "Курс по методике В. В. Голубиной",
            description = "Курс, основанный на методике В. В. Голубиной: символы изучаются" +
                    "в том порядке, как их придумал Луи Брайль для французского языка. " +
                    "Одновременно изучаются и цифры."
        ) {
            +golubinaIntroLessons
            +someMoreGolubinaLessons
        }

        annotations {
            +StepAnnotation.someBookRequired
        }
    }

    decks {
        deck("Русские буквы") { data ->
            data is Symbol && data.type == SymbolType.ru
        }
    }
}

private val golubinaIntroLessons by lessons {

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
        +Info(
            """
                Урок 1. Тренировка чтения и ввода отдельных комбинаций.
                <br><br>
                В рельефной азбуке Брайля любой символ - это шеститочие. 
                Каждая точка из шести может быть выдавлена или пропущена.
                В следующих шагах Вы ознакомитесь со всеми точками."""
        )
        +Info(
            """Точки расположены в два столбца по три. 
                Точки в первом столбце имеют номера 1, 2, 3 сверху вниз. 
                Точки во втором столбце - 4, 5, 6 сверху вниз. 
                Важно выучить, где какая точка."""
        )
        +ShowDots(
            text = "Перед Вами символ Брайля - <br>" +
                    " точка номер один",
            dots = BrailleDots(F, E, E, E, E, E)
        )
        +InputDots(
            text = "Введите точку один",
            dots = BrailleDots(F, E, E, E, E, E)
        )
        +ShowDots(
            text = "Точка номер два",
            dots = BrailleDots(E, F, E, E, E, E)
        )
        +InputDots(
            text = "Введите точку два",
            dots = BrailleDots(E, F, E, E, E, E)
        )
        +ShowDots(
            text = "Комбинация точек 1 и 2",
            dots = BrailleDots(F, F, E, E, E, E)
        )
        +InputDots(
            text = "Введите комбинацию: точки 1 и 2",
            dots = BrailleDots(F, F, E, E, E, E)
        )
        +InputDots(
            text = "Введите комбинацию точек 1 и 3",
            dots = BrailleDots(F, E, F, E, E, E)
        )
        +InputDots(
            text = "Введите комбинацию точек 1, 3 и 4",
            dots = BrailleDots(F, E, F, F, E, E)
        )
        +InputDots(
            // repeat = 3
            text = "Введите комбинацию точек 1, 3 и 6",
            dots = BrailleDots(F, E, F, E, E, F)
        )
        +InputDots(
            // repeat = 3
            text = "Введите комбинацию точек 1, 5 и 6",
            dots = BrailleDots(F, E, E, E, F, F)
        )
        +InputDots(
            // repeat = 3
            text = "Введите комбинацию точек 2, 3, 4 и 5",
            dots = BrailleDots(E, F, F, F, F, E)
        )
        +ShowDots(
            text = "Перед Вами полное шеститочие: <br>" +
                    "точки 1, 2, 3, 4, 5, 6",
            dots = BrailleDots(F, F, F, F, F, F)
        )
        +InputDots(
            // repeat = 3
            text = "Введите все шесть точек",
            dots = BrailleDots(F, F, F, F, F, F)
        )
        +Info(
            """Откройте букварь на странице 12.
                    В верхней строке 14 раз повторён символ полного шеститочия."""
        ).annotate(StepAnnotation.someBookRequired)
        +Info(
            """Первый урок закончен. В следующем уроке мы изучим буквы А, Б, Ц, Д, Е, Ф, Г."""
        )
    }

    lesson(
        name = "Русские буквы А, Б, Ц, Д, Е, Ф, Г"
    ) {
        +Info(
            """
                    Урок 2. Буквы А, Б, Ц, Д, Е, Ф, Г.
                    <br><br>
                    Некоторые символы Брайля обозначают как букву, так и цифру. Это буквы А, Б,
                    Ц и так далее. С добавлением цифрового знака, который мы изучим в следующем
                    уроке, из них получаются цифры 1, 2, 3 и так далее."""
        )
        +Info(
            """Буква А обозначается одной точкой, точкой номер один.
                    Ознакомьтесь с ней в следующем шаге."""
        )
        +Show(content.symbols.getValue('А'))
        +Info(
            """Откройте букварь на странице 13. Вверху слева рельефно-графическое
                    изображение буквы А. Рядом после полного шеститочия пять раз повторена 
                    буква А точечным шрифтом."""
        ).annotate(StepAnnotation.someBookRequired)
        +Input(content.symbols.getValue('А')/*, repeat = 5*/)
        +Show(content.symbols.getValue('Б'))
        +Info(
            """Снова изучим страницу 13 в букваре. Под строкой с буквой А - 
                    такая же с буквой Б."""
        ).annotate(StepAnnotation.someBookRequired)
        +Input(content.symbols.getValue('Б')/*, repeat = 5*/)
        +Info(
            """
                    Буква Ц: точки 1 и 4
                    <br><br>
                    Ознакомьтесь с буквой Ц на странице 13 букваря. 
                    Строка с буквой Ц находится под строкой с буквой Б."""
        ).annotate(StepAnnotation.someBookRequired)
        +Info(
            """В следующих шагах нужно изучить точечный состав и ввести
                    буквы Ц, Д, Е, Ф, Г"""
        )
        +Show(content.symbols.getValue('Ц'))
        +Input(content.symbols.getValue('Ц')/*, repeat = 5*/)
        +Show(content.symbols.getValue('Д'))
        +Input(content.symbols.getValue('Д')/*, repeat = 5*/)
        +Show(content.symbols.getValue('Е'))
        +Input(content.symbols.getValue('Е')/*, repeat = 5*/)
        +Show(content.symbols.getValue('Ф'))
        +Input(content.symbols.getValue('Ф')/*, repeat = 5*/)
        +Show(content.symbols.getValue('Г'))
        +Input(content.symbols.getValue('Г')/*, repeat = 5*/)
        +Info(
            """Поздравляем! Второй урок пройден. В следующем занятии мы узнаем, 
                    как с помощью букв А, Б, Ц, Д, Е, Ф, Г составить цифры 1, 2 3, 4, 5 и 6."""
        )
    }

    lesson(
        name = "Цифры от 1 до 6"
    ) {
        +Info(
            """
                    Урок 3. Цифры от 1 до 6.
                    <br><br>
                    В этом уроке мы начнём изучать цифры. Но для начала повторим пройденное.
                    В следующих шагах введите буквы, изученные в прошлом уроке."""
        )
        +Input(content.symbols.getValue('А'))
        +Input(content.symbols.getValue('Б'))
        +Input(content.symbols.getValue('Ц'))
        +Input(content.symbols.getValue('Д'))
        +Input(content.symbols.getValue('Е'))
        +Input(content.symbols.getValue('Ф'))
        +Input(content.symbols.getValue('Г'))
        +Info(
            """
                    Переходим к изучению цифр.
                    <br>
                    Чтобы отличить цифры от букв, перед числами и цифрами ставится цифровой знак.
                    Этот символ обозначается точками 3, 4, 5, 6.
                    Когда нужно обозначить цифровой знак в обычном, плоскопечатном письме, 
                    как правило, пользуются символом правой квадратной скобки: ]"""
        )
        +Show(content.symbols.getValue(']'))
        +Input(content.symbols.getValue(']')/*, repeat = 3*/)
        +Info(
            """
                    В следующих шагах изучим цифры 1, 2, 3, 4, 5, 6, 7.
                    Они получаются из букв А, Б, Ц, Д, Е, Ф, Г добавлением цифрового знака.
                    Например, цифра и число 3 - это цифровой знак + Ц.
                    Число двадцать семь - это цифровой знак, затем буквы Б и Г. 
                    Мы будем опускать цифровой знак."""
        )
        +Show(content.symbols.getValue('1'))
        +Input(content.symbols.getValue('1'))
        +Show(content.symbols.getValue('2'))
        +Input(content.symbols.getValue('2'))
        +Show(content.symbols.getValue('3'))
        +Input(content.symbols.getValue('3'))
        +Show(content.symbols.getValue('4'))
        +Input(content.symbols.getValue('4'))
        +Show(content.symbols.getValue('5'))
        +Input(content.symbols.getValue('5'))
        +Show(content.symbols.getValue('6'))
        +Input(content.symbols.getValue('6'))
        +Show(content.symbols.getValue('7'))
        +Input(content.symbols.getValue('7'))
        +Info(
            """
                    Урок 3 пройден! Рекомендуем самостоятельно изучить цифры и числа на странице 15
                    в букваре (внизу страницы).
                    <br>
                    Следующий урок будет посвящён повторению, а также мы изучим 
                    букву Х и цифру 8."""
        )
    }
}

private val someMoreGolubinaLessons by lessons {

    lesson(
        name = "Урок 4. Буква Х и цифра 8. Часть 1: повторение прошлого материала"
    ) {
        +Info(
            """
                    Урок 4. Буква Х и цифра 8.
                    <br><br>
                    По окончании этого урока Вы узнаете букву Х и цифру 8. 
                    Но перед изучением нового повторим пройденное."""
        )
        +Input(content.symbols.getValue('Б'))
        +Input(content.symbols.getValue('Е'))
        +Input(content.symbols.getValue('Г'))
        +Input(content.symbols.getValue(']'))
        +Input(content.symbols.getValue('Б'))
        +Input(content.symbols.getValue('А'))
        +Input(content.symbols.getValue('Г'))
        +Input(content.symbols.getValue('Д'))
        +Input(content.symbols.getValue('А'))
        +Input(content.symbols.getValue('Д'))
        +Input(content.symbols.getValue(']'))
        +Input(content.symbols.getValue('1'))
        +Input(content.symbols.getValue('2'))
        +Input(content.symbols.getValue('3'))
    }
    lesson(
        name = "Урок 4. Буква Х и цифра 8. Часть 2: новый материал"
    ) {
        +Info(
            """
                    Буква Х  обозначается точками 1, 2 и 5.
                    <br>
                    Цифра 8 - это цифровой знак и буква Х.
                    Потренируемся писать этот символ."""
        )
        +Show(content.symbols.getValue('Х'))
        +Input(content.symbols.getValue('Х'))
        +Input(content.symbols.getValue('8'))
        +Info(
            """
                    В следующих словах введите различные символы, в том числе буквы Х и цифру 8.
                    """
        )
        +Input(content.symbols.getValue('Ц'))
        +Input(content.symbols.getValue('Е'))
        +Input(content.symbols.getValue('Х'))
        +Input(content.symbols.getValue(']'))
        +Input(content.symbols.getValue('8') /*, repeat = 5*/)
        +Input(content.symbols.getValue('7'))
        +Input(content.symbols.getValue('6'))
        +Input(content.symbols.getValue('Б'))
        +Input(content.symbols.getValue('А'))
        +Input(content.symbols.getValue('Ц'))
        +Info(
            """
                    Урок 4 пройден! Материал о букве Х и цифре 8 находится на странице 16 в букваре.
                    Если у Вас есть возможность, изучите эту страницу самостоятельно.
                    <br>
                    На следующем занятии мы освоим буквы И и Ж, а также цифры 9 и 0"""
        )
    }

    lesson(
        name = "Заключение"
    ) {
        +LastInfo(
            """Вы дошли до конца курса. Спасибо, что воспользовались нашим обучающим
            приложением! Вы всегда можете вернутся к ранее пройденному материалу и повторить его.
            """
        )
    }
}
