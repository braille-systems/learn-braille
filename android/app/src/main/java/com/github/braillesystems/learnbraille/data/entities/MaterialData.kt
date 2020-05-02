package com.github.braillesystems.learnbraille.data.entities


sealed class MaterialData


typealias SymbolType = String

data class Symbol(
    val symbol: Char,
    val brailleDots: BrailleDots,
    val type: SymbolType
) : MaterialData()