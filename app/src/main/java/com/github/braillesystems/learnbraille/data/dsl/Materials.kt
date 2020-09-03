package com.github.braillesystems.learnbraille.data.dsl

import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.res.MarkerType
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

    private val _markers = mutableMapOf<MarkerType, Material>()
    val markers: Map<MarkerType, Material>
        get() = _markers

    init {
        block()
        _materials = materials.mapIndexed { index, material ->
            material.copy(id = index + 1L).apply {
                when (data) {
                    is Symbol -> {
                        require(!_symbols.contains(data.char)) {
                            "Symbol (symbol = ${data.char}, type = ${data.type}) already exists"
                        }
                        _symbols[data.char] = this
                    }
                    is MarkerSymbol -> {
                        require(!_markers.contains(data.type)) {
                            "Symbol (type = ${data.type}"
                        }
                        _markers[data.type] = this
                    }
                }
            }
        }.toMutableList()
    }

    operator fun SymbolsBuilder.unaryPlus() {
        symbols.forEach { symbol ->
            this@MaterialsBuilder._materials.add(Material(UNDEFINED_ID, symbol))
        }
    }

    operator fun MarkersBuilder.unaryPlus() {
        markers.forEach { marker ->
            this@MaterialsBuilder._materials.add(Material(UNDEFINED_ID, marker))
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

class markers(private val block: MarkersBuilder.() -> Unit) {

    private var markers: MarkersBuilder? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>) =
        markers ?: MarkersBuilder(block).also { markers = it }
}

class MarkersBuilder(private val block: MarkersBuilder.() -> Unit) {

    private val _map = mutableMapOf<MarkerType, MarkerSymbol>()
    val map: Map<MarkerType, MarkerSymbol> get() = _map
    internal val markers: List<MarkerSymbol>
        get() = _map.values.toList()

    init {
        block()
    }

    operator fun get(type: MarkerType): MarkerSymbol? = _map[type]

    fun marker(type: MarkerType, brailleDots: BrailleDots) {
        _map[type] = MarkerSymbol(type, brailleDots)
    }
}

class known(vararg chars: Char) {

    private val cs = chars.map(Char::toUpperCase)
    private var knownMaterials: List<KnownMaterial>? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): List<KnownMaterial> =
        knownMaterials
            ?: cs
                .map { KnownMaterial(UNDEFINED_ID, content.symbols.getValue(it).id) }
                .also { knownMaterials = it }
}
