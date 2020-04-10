package ru.spbstu.amd.learnbraille.database

import androidx.fragment.app.Fragment

fun Fragment.getDBInstance() = LearnBrailleDatabase.getInstance(
    requireNotNull(activity).application
)
