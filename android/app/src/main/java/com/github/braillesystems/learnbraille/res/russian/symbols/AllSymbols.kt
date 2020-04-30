package com.github.braillesystems.learnbraille.res.russian.symbols

import com.github.braillesystems.learnbraille.data.types.Symbol

/**
 * Add via plus lists of symbols
 */
val PREPOPULATE_SYMBOLS get() = PUNCTUATION + DIGITS + RU_LETTERS

val symbolMap: Map<Char, Symbol> = PREPOPULATE_SYMBOLS
    .groupBy { it.symbol }
    .mapValues {
        require(it.value.size == 1) {
            "Each symbol should appear only once in lists"
        }
        it.value.first()
    }
