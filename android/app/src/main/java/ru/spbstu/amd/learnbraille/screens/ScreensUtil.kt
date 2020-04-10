package ru.spbstu.amd.learnbraille.screens

import android.app.Application
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.spbstu.amd.learnbraille.BuzzPattern
import timber.log.Timber

fun Fragment.updateTitle(title: String) {
    (activity as AppCompatActivity)
        .supportActionBar
        ?.title = title
}

fun Fragment.getStringArg(name: String): String =
    arguments?.getString(name) ?: error("No $name found in args")

fun Vibrator?.buzz(pattern: BuzzPattern) {
    if (this == null) {
        Timber.i("Vibrator is not available")
        return
    }
    // Use deprecated API to be compatible with old android API levels
    @Suppress("DEPRECATION")
    vibrate(pattern, -1)
}

val Fragment.application: Application
    get() = requireNotNull(activity).application
