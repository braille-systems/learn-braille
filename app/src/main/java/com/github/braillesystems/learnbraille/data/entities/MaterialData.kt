package com.github.braillesystems.learnbraille.data.entities

import androidx.room.TypeConverter
import com.github.braillesystems.learnbraille.res.MarkerType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
sealed class MaterialData

class MaterialDataTypeConverters {

    @TypeConverter
    fun to(md: MaterialData): String = Json.encodeToString(MaterialData.serializer(), md)

    @TypeConverter
    fun from(s: String): MaterialData = Json.decodeFromString(MaterialData.serializer(), s)
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
