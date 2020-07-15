package com.github.braillesystems.learnbraille.data.dsl

import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.entities.KnownMaterial
import com.github.braillesystems.learnbraille.data.entities.Material
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.res.content
import kotlin.reflect.KProperty


open class materials(private val block: MaterialsBuilder.() -> Unit) {

    private var materials: MaterialsBuilder? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>) =
        materials ?: MaterialsBuilder(block).also { materials = it }
}

@DataBuilderMarker
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
                    require(!_symbols.contains(data.char)) {
                        "Symbol (symbol = ${data.char}, type = ${data.type}) already exists"
                    }
                    _symbols[data.char] = this
                }
            }
        }.toMutableList()
    }

    operator fun SymbolsBuilder.unaryPlus() {
        symbols.forEach { symbol ->
            this@MaterialsBuilder._materials.add(Material(DEFAULT_ID, symbol))
        }
    }
}


class symbols(private val symbolType: String, private val block: SymbolsBuilder.() -> Unit) {

    private var symbols: SymbolsBuilder? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>) =
        symbols ?: SymbolsBuilder(symbolType, block).also { symbols = it }
}

@DataBuilderMarker
class SymbolsBuilder(private val symbolType: String, block: SymbolsBuilder.() -> Unit) {

    private val _map = mutableMapOf<Char, Symbol>()
    val map: Map<Char, Symbol> get() = _map
    internal val symbols: List<Symbol>
        get() = _map.values.toList()

    init {
        block()
    }

    operator fun get(symbol: Char): Symbol? = _map[symbol]

    fun symbol(char: Char, brailleDots: BrailleDots) {
        @Suppress("NAME_SHADOWING") val char = char.toUpperCase()
        _map[char] = Symbol(char, brailleDots, symbolType)
    }
}


class known(vararg chars: Char) {

    private val cs = chars.map(Char::toUpperCase)
    private var knownMaterials: List<KnownMaterial>? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): List<KnownMaterial> =
        knownMaterials ?: cs.map {
            KnownMaterial(DEFAULT_ID, content.symbols.getValue(it).id)
        }.also { knownMaterials = it }
}
