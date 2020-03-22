package ru.spbstu.amd.learnbraille.res.russian.symbols

import ru.spbstu.amd.learnbraille.database.Symbol

/**
 * Add via plus lists of symbols
 */
val PREPOPULATE_SYMBOLS = RU_LETTERS + OTHER + PUNCTUATION + DIGITS

val symbolMap: Map<Char, Symbol> = PREPOPULATE_SYMBOLS
    .groupBy { it.symbol }
    .mapValues {
        require(it.value.size == 1) {
            "Each symbol should appear only once in lists"
        }
        it.value.first()
    }
