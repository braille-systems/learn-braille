package com.github.braillesystems.learnbraille.data.entities

import androidx.room.TypeConverter

enum class Language {
    NONE, EN, RU
}

class LanguageConverters {

    @TypeConverter
    fun to(language: Language) = language.toString()

    @TypeConverter
    fun from(data: String) = Language.valueOf(data)
}
