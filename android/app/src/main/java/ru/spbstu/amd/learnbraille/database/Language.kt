package ru.spbstu.amd.learnbraille.database

import androidx.room.TypeConverter

enum class Language {
    NONE, EN, RU
}

class LanguageConverter {

    @TypeConverter
    fun to(language: Language) = language.toString()

    @TypeConverter
    fun from(data: String) = Language.valueOf(data)
}