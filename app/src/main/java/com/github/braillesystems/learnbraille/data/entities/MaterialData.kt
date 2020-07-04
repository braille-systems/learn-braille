package com.github.braillesystems.learnbraille.data.entities

import androidx.room.TypeConverter
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


@Serializable
data class Symbol(
    val char: Char,
    val brailleDots: BrailleDots,
    @SerialName("symbol_type")
    val type: String
) : MaterialData()
