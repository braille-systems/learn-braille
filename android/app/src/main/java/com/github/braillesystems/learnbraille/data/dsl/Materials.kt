package com.github.braillesystems.learnbraille.data.dsl

import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.entities.Material
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.data.entities.SymbolType
import kotlin.reflect.KProperty


open class materials(private val block: MaterialsBuilder.() -> Unit) {

    private var materials: MaterialsBuilder? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>) =
        materials ?: MaterialsBuilder(block).also { materials = it }
}

class MaterialsBuilder(block: MaterialsBuilder.() -> Unit) {

    private var _materials = mutableListOf<Material>()
    internal val materials: List<Material>
        get() = _materials

    private val _symbols = mutableMapOf<Char, Material>()
    val symbols: Map<Char, Material>
        get() = _symbols

    init {
        block()
        _materials = materials.mapIndexed { index, material ->
            material.copy(id = index + 1L).apply {
                if (data is Symbol) {
                    require(!_symbols.contains(data.symbol)) {
                        "Symbol (symbol = ${data.symbol}, type = ${data.type}) already exists"
                    }
                    _symbols[data.symbol] = this
                }
            }
        }.toMutableList()
    }

    operator fun SymbolsBuilder.unaryPlus() {
        symbols.forEach { symbol ->
            _materials.add(Material(DEFAULT_ID, symbol))
        }
    }
}


class symbols(private val type: SymbolType, private val block: SymbolsBuilder.() -> Unit) {

    private var symbols: SymbolsBuilder? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>) =
        symbols ?: SymbolsBuilder(type, block).also { symbols = it }
}

class SymbolsBuilder(val type: SymbolType, block: SymbolsBuilder.() -> Unit) {

    private var map = mutableMapOf<Char, Symbol>()
    internal val symbols: List<Symbol>
        get() = map.values.toList()

    init {
        block()
    }

    operator fun get(symbol: Char): Symbol? = map[symbol]

    fun symbol(symbol: Char, brailleDots: BrailleDots) {
        map[symbol] = Symbol(symbol, brailleDots, type)
    }
}
