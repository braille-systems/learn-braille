package com.github.braillesystems.learnbraille.data.dsl

const val UNDEFINED_ID = -1L
const val ALL_CARDS_DECK_ID = 1L

object InfoInterpolation {
    const val iStep = "#istep"
    const val iLesson = "#ilesson"
    const val courseName = "#courseName"
}

@DslMarker
internal annotation class DataBuilderMarker
