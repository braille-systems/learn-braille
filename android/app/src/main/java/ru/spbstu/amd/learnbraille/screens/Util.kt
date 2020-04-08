package ru.spbstu.amd.learnbraille.screens

import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.spbstu.amd.learnbraille.BuzzPattern

fun Fragment.updateTitle(title: String) {
    (activity as AppCompatActivity)
        .supportActionBar
        ?.title = title
}

fun Vibrator?.buzz(pattern: BuzzPattern) {
    if (this == null) return
    // Use deprecated API to be compatible with old android API levels
    @Suppress("DEPRECATION")
    vibrate(pattern, -1)
}
