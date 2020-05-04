package com.github.braillesystems.learnbraille.data.dsl

import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.entities.Material
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.data.entities.SymbolType
import kotlin.reflect.KProperty


open class materials(private val block: _Materials.() -> Unit) {

    private var materials: _Materials? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>) =
        materials ?: _Materials(block).also { materials = it }
}

class _Materials(block: _Materials.() -> Unit) {

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

    operator fun _Symbols.unaryPlus() {
        symbols.forEach { symbol ->
            _materials.add(Material(DEFAULT_ID, symbol))
        }
    }
}


class symbols(private val type: SymbolType, private val block: _Symbols.() -> Unit) {

    private var symbols: _Symbols? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>) =
        symbols ?: _Symbols(type, block).also { symbols = it }
}

class _Symbols(val type: SymbolType, block: _Symbols.() -> Unit) {

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
