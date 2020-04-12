package ru.spbstu.amd.learnbraille.database

import androidx.fragment.app.Fragment

// TODO move utils to separate package

fun Fragment.getDBInstance() = LearnBrailleDatabase.getInstance(
    requireNotNull(activity).application
)
