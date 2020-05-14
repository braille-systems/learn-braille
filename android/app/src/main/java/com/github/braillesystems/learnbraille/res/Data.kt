package com.github.braillesystems.learnbraille.res

import com.github.braillesystems.learnbraille.data.dsl.StepsBuilder
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
 *
 * If using `content.symbols.getValue`, `content` should be added to `data` as `materials`.
 * It is better to simply have only one value declared as `by materials`.
 *
 * Correctness of all information should be checked in compile time or in runtime.
 * If you need some additional info, do not hardcode it. Just make request to the new DSL feature.
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

private fun StepsBuilder.inputChars(s: String) {
    // TODO input as real word
    for (ch in s){
        +Input(content.symbols.getValue(ch));
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
        name = "Русские буквы А, Б, Ц, Д, Е"
    ) {
        +Info(
            """
                    Урок 2. Буквы А, Б, Ц, Д, Е.
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
            """Буква Ц обозначается точками 1 и 4,
                    буква Д - точки 1, 4 и 5.
                    В следующих шагах нужно изучить точечный состав и ввести
                    буквы Ц, Д"""
        )
        +Show(content.symbols.getValue('Ц'))
        +Input(content.symbols.getValue('Ц')/*, repeat = 5*/)
        +Show(content.symbols.getValue('Д'))
        +Input(content.symbols.getValue('Д')/*, repeat = 5*/)
        +Info(
            """Последняя буква в этом уроке - буква Е. 
                    Она обозначается двумя точками: номер 1 и 5."""
        )
        +Show(content.symbols.getValue('Е'))
        +Input(content.symbols.getValue('Е')/*, repeat = 5*/)
        +Info(
            """Урок 2 закончен. В следующем занятии займёмся повторением букв А, Б, Ц, Д, Е
                 и выучим буквы Ф, Г."""
        )
    }
    lesson(
        name = "Буквы Ф, Г"
    ) {
        +Info(
            """
                    Урок 3. Буквы Ф, Г.
                    <br><br>
                    Перед прохождением нового материала повторим пройденное.
                    В следующих трёх шагах нужно ввести буквы Б, А, Ц (вместе это слово БАЦ)."""
        )
        inputChars("БАЦ")
        +Info(
            """
                    Теперь познакомимся с буквами Ф и Г.
                    <br>
                    Буква Ф обозначается точками 1, 2 и 4.
                    <br>
                    Буква Г - точки 1, 2, 4 и 5.
                    <br>
                    В следующих четырёх шагах нужно ознакомиться с буквами и ввести их."""
        )
        +Show(content.symbols.getValue('Ф'))
        +Input(content.symbols.getValue('Ф')/*, repeat = 5*/)
        +Show(content.symbols.getValue('Г'))
        +Input(content.symbols.getValue('Г')/*, repeat = 5*/)
        +Info(
            """
                    В следующих трёх шагах введите по буквам слово БЕГ."""
        )
        inputChars("БЕГ")
        +Info(
            """Поздравляем! Второй урок пройден. В следующем занятии мы узнаем, 
                    как с помощью букв А, Б, Ц, Д, Е, Ф, Г составить цифры 1, 2, 3, 4, 5 и 6."""
        )
    }

    lesson(
        name = "Цифры от 1 до 6"
    ) {
        +Info(
            """
                    Урок 4. Цифры от 1 до 6.
                    <br><br>
                    В этом уроке мы начнём изучать цифры. Но для начала повторим пройденное.
                    В следующих шагах введите буквы, изученные в прошлых двух уроках."""
        )
        inputChars("АБЦДЕФГ")
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
                    В уроках мы для краткости не будем всякий раз ставить цифровой знак."""
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
                    Урок 4 пройден! Рекомендуем самостоятельно изучить цифры и числа на странице 15
                    в букваре (внизу страницы).
                    <br>
                    Следующий урок будет посвящён повторению, а также мы изучим 
                    букву Х и цифру 8."""
        )
    }
}

/*
* In lessons with words input I tried to bear a scheme '6+-2 words'
* but there are several exceptions, e. g. where words are especially long/short
* or when new symbols are difficult
* */

private val someMoreGolubinaLessons by lessons {

    lesson(
        name = "Буква Х и цифра 8. Часть 1: повторение прошлого материала"
    ) {
        +Info(
            """
                    Урок 5. Буква Х и цифра 8.
                    <br><br>
                    По окончании этого урока Вы узнаете букву Х и цифру 8. 
                    Но перед изучением нового повторим пройденное. Введите по буквам слово ФЕБ. """
        )
        inputChars("ФЕБ")
        +Info(
            """
                    В следующих шести шагах введите по буквам слово БАГДАД."""
        )
        inputChars("БАГДАД")
        inputChars("]123")
    }
    lesson(
        name = "Буква Х и цифра 8. Часть 2: новый материал"
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
                    В следующих шагах введите различные символы, в том числе буквы Х и цифру 8.
                    """
        )
        inputChars("ЦЕХ")
        +Input(content.symbols.getValue(']'))
        +Input(content.symbols.getValue('8') /*, repeat = 5*/)
        +Input(content.symbols.getValue('7'))
        +Input(content.symbols.getValue('6'))
        inputChars("БАЦ")
        +Info(
            """
                    Урок 5 пройден! Материал о букве Х и цифре 8 находится на странице 16 в букваре.
                    Если у Вас есть возможность, изучите эту страницу самостоятельно.
                    <br>
                    На следующем занятии мы освоим буквы И и Ж, а также цифры 9 и 0"""
        )
    }

    lesson(
        name = "Буквы Ж, И и цифры 9, 0"
    ) {
        +Info(
            """
                    Урок 6. Буквы Ж, И и цифры 9, 0
                    <br><br>
                    Буква И - это точки 2 и 4.
                    Этот же символ с добавлением цифрового знака обозначает цифру 9.
                    <br>
                    Буква Ж - это точки 2, 4 и 5.
                    Цифровой знак и буква Ж образуют цифру 0.
                    <br>
                    Введите символы, которые образуют слово ЖАБА"""
        )
        inputChars("ЖАБА")
        +Info(
            """Введите буквы, составляющие слово БАГАЖ"""
        )
        inputChars("БАГАЖ")
        +Info(
            """Введите буквы, составляющие слово ЖАЖДА"""
        )
        inputChars("ЖАЖДА")
        +Info(
            """Напомним, буква И кодируется двумя точками с номерами 2 и 4.
                    Введите буквы, образующие слово ГИД"""
        )
        inputChars("ГИД")
        +Info(
            """Введите буквы, которые образуют слово ИЖИЦА"""
        )
        inputChars("ИЖИЦА")
        +Info(
            """Введите по символам число 850"""
        )
        inputChars("]850")
        +Info(
            """Введите разные цифры"""
        )
        +Input(content.symbols.getValue('1'))
        +Input(content.symbols.getValue('3'))
        +Input(content.symbols.getValue('2'))
        +Input(content.symbols.getValue('6'))
        +Input(content.symbols.getValue('9'))
        +Input(content.symbols.getValue('7'))
        +Info(
            """
                    Урок 6 закончен. Теперь Вы, помимо букв, знаете все цифры.
                    Буквы И, Ж и примеры к ним можно найти в букваре на странице 17.
                    <br>
                    Следующий урок посвящён буквам К, Л и М."""
        )
    }

    lesson(
        name = "Буквы К, Л, М. Часть 1: Буквы К, Л"
    ) {
        +Info(
            """
                    Урок 7. Буквы К, Л, М
                    <br><br>
                    До сих пор мы изучали только буквы, образованные верхними четырьмя точками,
                    то есть точками 1, 2, 4, 5. В нескольких следующих уроках будут рассмотрены
                    буквы, содержащие, помимо того, точку три.
                    <br>
                    Перед занятием повторим пройденное. Введите по буквам слово ЕЖИХА.
                    """
        )
        inputChars("ЕЖИХА")
        +Info(
            """
                    Теперь перейдём к изучению материала.
                    <br>
                    Буква К: точки 1 и 3. Заметьте, она получается добавлением точки 3 к букве А.
                    <br>
                    Буква Л: точки 1, 2, 3. Как буква Б, но ещё с точкой 3.
                    <br>
                    Ознакомьтесь с этими буквами в следующих двух шагах.
                    """
        )
        +Show(content.symbols.getValue('К'))
        +Show(content.symbols.getValue('Л'))
        +Info(
            """Введите по буквам слово КЛАД"""
        )
        inputChars("КЛАД")
        +Info(
            """Введите по буквам слово БЕЛКА"""
        )
        inputChars("БЕЛКА")
        +Info(
            """Теперь введите отдельными символами слово ФЛАГ"""
        )
        inputChars("ФЛАГ")
    }

    lesson(
        name = "Буквы К, Л, М. Часть 2: Буква М"
    ) {
        +Info(
            """
                    Сегодня осталось изучить букву М.
                    <br>
                    Буква М обозначается точками 1, 3 и 4. Она получается из буквы Ц 
                    при добавлении точки 3.
                    <br>
                    В следующем шаге буква М выведена на экран.
                    """
        )
        +Show(content.symbols.getValue('М'))
        +Info(
            """В следующих трёх шагах ведите по буквам слово МЕЛ"""
        )
        inputChars("МЕЛ")
        +Info(
            """Введите по буквам слово МАК"""
        )
        inputChars("МАК")
        +Info(
            """Напоследок введите по буквам слово ФИАЛКА"""
        )
        inputChars("ФИАЛКА")
        +Info(
            """
                    Мы подошли к концу урока 7.
                    Буквам К, Л, М посвящены страницы 19 и 20 букваря.
                    <br>
                    На следующем занятии мы узнаем, как обозначается буква Н."""
        )
    }

    lesson(
        name = "Буква Н"
    ) {
        +Info(
            """
                    Урок 8. Буква Н.
                    <br><br>
                    Рекомендуем в дополнение к этому уроку изучить букву Н 
                    на страницах 21 и 22 букваря.
                    <br>
                    Начнём занятие с повторения. Введите по буквам слово КАМБАЛА.
                    """
        )
        inputChars("КАМБАЛА")
        +Info(
            """
                    Отлично, теперь познакомимся с буквой Н.
                    <br>
                    Буква Н обозначается четырьмя точками: 1, 3, 4 и 5. 
                    Она образована точками буквы Д с добавлением точки номер 3.
                    <br>
                    В следующем шаге ознакомьтесь с этой буквой.
                    """
        )
        +Show(content.symbols.getValue('Н'))
        +Input(content.symbols.getValue('Н'))
        +Info(
            """Введите по буквам слово БАНАН"""
        )
        inputChars("БАНАН")
        +Info(
            """Введите по буквам слово ЦЕНА"""
        )
        inputChars("ЦЕНА")
        +Info(
            """Теперь введите по буквам слово БЛАНК"""
        )
        inputChars("БЛАНК")
        +Info(
            """
                    На этом урок 8 завершается.
                    <br>
                    В следующий раз изучим букву О и знак препинания ЗАПЯТАЯ.
                    """
        )
    }
    lesson(
        name = "Буква О и знак препинания 'Запятая'"
    ) {
        +Info(
            """
                    Урок 9. Буква О и знак препинания "Запятая".
                    <br><br>
                    Материалы к этому уроку есть на страницах 23-24 в букваре.
                    <br>
                    Как обычно, в начале занятия повторим пройденное.
                    Введите по буквам слово НАДЕЖДА.
                    """
        )
        inputChars("НАДЕЖДА")
        +Info(
            """
                    Идём дальше: теперь изучим букву 'О'.
                    <br>
                    Буква 'О' - это точки 1, 3 и 5. Если помните, точками 1 и 5 
                    обозначается буква Е, а О образуется из Е добавлением точки 3.
                    """
        )
        +Show(content.symbols.getValue('О'))
        +Info(
            """Наберите по буквам слово ОБЛАКО"""
        )
        inputChars("ОБЛАКО")
        +Info(
            """Теперь введите по буквам: ОКНО"""
        )
        inputChars("ОКНО")
    }
    lesson(
        name = "Буква О и знак препинания 'Запятая'. Часть 2: 'Запятая'"
    ) {
        +Info(
            """
                    Кратко ознакомимся со знаком 'Запятая'.
                    <br>
                    Запятая, как и буква А - только одна точка. Но если А - это точка 1, то запятая
                    - точка 2.
                    """
        )
        +Show(content.symbols.getValue(','))
        +Info(
            """Наберите по символам слово ЛОДКА и поставьте в конце запятую"""
        )
        inputChars("ЛОДКА,")
        +Info(
            """Аналогично предыдущему, введите слово КОФЕ и запятую"""
        )
        inputChars("КОФЕ,")
        +Info(
            """Напоследок введите слово ГЕОЛОГ и тоже поставьте запятую в конце"""
        )
        inputChars("ГЕОЛОГ,")
        +Info(
            """Урок 9 подходит к концу. В следующем уроке нас ждёт изучение буквы П."""
        )
    }
    lesson(
        name = "Буква П"
    ) {
        +Info(
            """
                    Урок 10. Буква П.
                    <br><br>
                    Этот урок полезно закрепить, изучив страницу 25 в книге Голубиной.
                    <br>
                    В виде разминки перед уроком повторим изученные ранее символы.
                    Введите по буквам слово МОЛОКО и поставьте после него запятую.
                    """
        )
        inputChars("МОЛОКО,")
        +Info(
            """
                    Давайте познакомимся с буквой 'П'.
                    <br>
                    Буква 'П' представлена точками 1, 2, 3 и 4. Напомним, точки 1, 2 и 4 образуют
                    букву Ф. Букву П можно получить из Ф, добавив точку номер 3.
                    """
        )
        +Show(content.symbols.getValue('П'))
        +Info(
            """Введите отдельными символами слово ПЕНА"""
        )
        inputChars("ПЕНА")
        +Info(
            """Теперь нужно набрать по буквам слово ЛАМПА"""
        )
        inputChars("ЛАМПА")
        +Info(
            """Наберите по буквам слово ПЛАН"""
        )
        inputChars("ПЛАН")
        +Info(
            """И последнее в этом уроке: введите по буквам слово КАПКАН"""
        )
        inputChars("КАПКАН")
        +Info(
            """Вот и пройден десятый урок. Следующее занятие отведено для изучения буквы Ч"""
        )
    }

    lesson(
        name = "Буква Ч"
    ) {
        +Info(
            """
                    Урок 11. Буква Ч.
                    <br><br>
                    Буква Ч и слова с ней находятся на странице 26 в букваре Голубиной.
                    <br>
                    Перед началом работы немного повторим прошлые уроки.
                    Введите по буквам слово ХЛОПОК.
                    """
        )
        inputChars("ХЛОПОК")
        +Info(
            """Введите отдельными символами число 215, поставив перед ним цифровой знак"""
        )
        inputChars("]215")

        +Info(
            """
                    Переходим к изучению новой буквы: 'Ч'.
                    <br>
                    Русская буква 'Ч' в азбуке Брайля составлена из точек 1, 2, 3, 4 и 5. 
                    Её можно получить из буквы Г, если дополнить точкой 3.
                    """
        )
        +Show(content.symbols.getValue('Г'))
        +Info(
            """Введите по буквам слово ЧЕК"""
        )
        inputChars("ЧЕК")
        +Info(
            """Наберите отдельными буквами слово ОЧКИ"""
        )
        inputChars("ОЧКИ")
        +Info(
            """Далее введите по буквам слово КОЧАН"""
        )
        inputChars("КОЧАН")
        +Info(
            """Введите по буквам ещё одно, последнее слово: БОЧОНОК"""
        )
        inputChars("БОЧОНОК")
        +Info(
            """
                    Урок 11, в котором мы изучали букву 'Ч', позади. 
                    А в следующий раз мы будем заниматься буквой Р.
                    """
        )
    }
    lesson(
        name = "Буква Р"
    ) {
        +Info(
            """
                    Урок 12. Буква Р.
                    <br><br>
                    Этому уроку соответствует страница 27 букваря.
                    <br>
                    Перед стартом повторим материал прошлых уроков. 
                    Введите по символам слово ПЧЕЛА.
                    """
        )
        inputChars("ПЧЕЛА")
        +Info(
            """
                    Сегодня мы изучим букву 'Р'.
                    <br>
                    Буква 'Р' формируется из точек с номерами 1, 2, 3 и 5. 
                    Иначе говоря, буква Р - это буква Х и точка 3.
                    """
        )
        +Show(content.symbols.getValue('Р'))
        +Info(
            """Наберите последовательно, по буквам слово РЕКА"""
        )
        inputChars("РЕКА")
        +Info(
            """Наберите отдельными символами слово КРАН"""
        )
        inputChars("КРАН")
        +Info(
            """В четырёх шагах введите по буквам слово КРЕМ"""
        )
        inputChars("КРЕМ")
        +Info(
            """Теперь введите по символам слово ФАРА"""
        )
        inputChars("ФАРА")
        +Info(
            """Наберите аналогичным образом последнее слово в сегодняшнем уроке: ГАРАЖ"""
        )
        inputChars("ГАРАЖ")
        +Info(
            """
                    Урок 12 завершён. Теперь мы, помимо прочего, знаем точечный состав буквы 'Ч'.
                    За этим уроком следует занятие, посвящённое букве С.
                    """
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
