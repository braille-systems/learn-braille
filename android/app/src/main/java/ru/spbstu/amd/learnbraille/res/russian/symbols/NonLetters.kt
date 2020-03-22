package ru.spbstu.amd.learnbraille.res.russian.symbols

import ru.spbstu.amd.learnbraille.database.BrailleDot.F
import ru.spbstu.amd.learnbraille.database.BrailleDots
import ru.spbstu.amd.learnbraille.database.Language.NONE
import ru.spbstu.amd.learnbraille.database.Symbol

const val FILLED_SYMBOL = Char.MAX_VALUE

val OTHER = listOf(
    Symbol(symbol = FILLED_SYMBOL, language = NONE, brailleDots = BrailleDots(F, F, F, F, F, F))
)

// TODO fill punctuation
val PUNCTUATION = listOf<Symbol>()

// TODO fill digits
val DIGITS = listOf<Symbol>()
