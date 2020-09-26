package com.github.braillesystems.learnbraille.data.entities

import androidx.room.TypeConverter
import com.github.braillesystems.learnbraille.res.MarkerType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

@Serializable
sealed class MaterialData

class MaterialDataTypeConverters {

    @UnstableDefault
    @TypeConverter
    fun to(md: MaterialData): String = Json.stringify(MaterialData.serializer(), md)

    @UnstableDefault
    @TypeConverter
    fun from(s: String): MaterialData = Json.parse(MaterialData.serializer(), s)
}

typealias SymbolType = String

@Serializable
sealed class OneBrailleSymbol : MaterialData() {
    abstract val brailleDots: BrailleDots
}

@Serializable
data class Symbol(
    val char: Char,
    override val brailleDots: BrailleDots,
    @SerialName("symbol_type")
    val type: SymbolType
) : OneBrailleSymbol()

@Serializable
data class MarkerSymbol(
    @SerialName("marker_type")
    val type: MarkerType,
    override val brailleDots: BrailleDots
) : OneBrailleSymbol()
