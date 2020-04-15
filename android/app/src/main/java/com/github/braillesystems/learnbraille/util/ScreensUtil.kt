package com.github.braillesystems.learnbraille.util

import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.BuzzPattern
import com.github.braillesystems.learnbraille.LearnBrailleApplication
import timber.log.Timber

fun Fragment.updateTitle(title: String) {
    (activity as AppCompatActivity)
        .supportActionBar
        ?.title = title
}

fun Fragment.getStringArg(name: String): String =
    arguments?.getString(name) ?: error("No $name found in args")

fun Vibrator?.buzz(pattern: BuzzPattern): Unit =
    if (this == null) Timber.i("Vibrator is not available")
    else {
        // Use deprecated API to be compatible with old android API levels
        @Suppress("DEPRECATION")
        vibrate(pattern, -1)
    }

val Fragment.application: LearnBrailleApplication
    get() = requireNotNull(activity).application as LearnBrailleApplication
