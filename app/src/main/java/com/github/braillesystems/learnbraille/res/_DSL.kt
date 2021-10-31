package com.github.braillesystems.learnbraille.res

import com.github.braillesystems.learnbraille.data.dsl.StepsBuilder
import com.github.braillesystems.learnbraille.data.dsl.annotate
import com.github.braillesystems.learnbraille.data.entities.*

fun StepsBuilder.inputChars(chars: String): Unit =
    chars
        .map(Char::toUpperCase)
        .forEach { c -> +Input(content.symbols.getValue(c)) }

fun StepsBuilder.inputPhraseByLetters(phrase: String) {
    val materials = phrase
        .map(Char::toUpperCase)
        .map(content.symbols::getValue)
    materials.forEachIndexed { i, _ ->
        +InputPhraseLetter(materials, i)
    }
}

fun StepsBuilder.showAndInputChars(chars: String): Unit =
    chars.map(Char::toUpperCase).forEach {
        +Show(content.symbols.getValue(it))
        +Input(content.symbols.getValue(it))
    }

fun StepsBuilder.showAndInputMarkers(markers: List<MarkerType>) {
    markers.forEach {
        +Show(content.markers.getValue(it))
        +Input(content.markers.getValue(it))
    }
}

fun StepsBuilder.slateStylusLine(char: Char) {
    +Info(
        "Запишите на брайлевском приборе строку, состоящую из одного символа: $char."
    ).annotate(StepAnnotation.slateStylusRequired)
}

fun StepsBuilder.inputNumber(number: Int) {
    +Input(content.markers.getValue(MarkerType.NumberSign))
    inputChars(number.toString())
}
