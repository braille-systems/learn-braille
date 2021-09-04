package com.github.braillesystems.learnbraille.res

import com.github.braillesystems.learnbraille.data.dsl.StepsBuilder
import com.github.braillesystems.learnbraille.data.dsl.annotate
import com.github.braillesystems.learnbraille.data.entities.Info
import com.github.braillesystems.learnbraille.data.entities.Input
import com.github.braillesystems.learnbraille.data.entities.Show

fun StepsBuilder.inputChars(chars: String): Unit =
    chars
        .map(Char::toUpperCase)
        .forEach { c -> +Input(content.symbols.getValue(c)) }

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
