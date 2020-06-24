package com.github.braillesystems.learnbraille.res

import com.github.braillesystems.learnbraille.data.dsl.StepsBuilder
import com.github.braillesystems.learnbraille.data.entities.Input

fun StepsBuilder.inputChars(s: String): Unit =
    s.map(Char::toUpperCase).forEach { c -> +Input(content.symbols.getValue(c)) }
