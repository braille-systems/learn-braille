package ru.spbstu.amd.learnbraille.res.russian.symbols

import ru.spbstu.amd.learnbraille.database.entities.Symbol

/**
 * Add via plus lists of symbols
 */
val PREPOPULATE_SYMBOLS = PUNCTUATION + DIGITS + RU_LETTERS

val symbolMap: Map<Char, Symbol> = PREPOPULATE_SYMBOLS
    .groupBy { it.symbol }
    .mapValues {
        require(it.value.size == 1) {
            "Each symbol should appear only once in lists"
        }
        it.value.first()
    }
