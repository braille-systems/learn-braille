package ru.spbstu.amd.learnbraille.screens

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.updateTitle(title: String) {
    (activity as AppCompatActivity)
        .supportActionBar
        ?.title = title
}
