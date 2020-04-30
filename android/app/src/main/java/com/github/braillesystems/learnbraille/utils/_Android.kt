package com.github.braillesystems.learnbraille.utils

import android.app.Application
import android.content.Context
import android.hardware.usb.UsbManager
import android.os.Vibrator
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import timber.log.Timber

val Application.usbManager get() = getSystemService(Context.USB_SERVICE) as UsbManager

val Fragment.actionBar: ActionBar?
    get() = (activity as AppCompatActivity).supportActionBar

/**
 * Throws if action bar is not available
 */
var Fragment.title: String
    get() = requireNotNull(actionBar).title.toString()
    set(value) {
        requireNotNull(actionBar).title = value
    }

fun Fragment.updateTitle(title: String) {
    this.title = title
}

fun Fragment.getStringArg(name: String): String =
    arguments?.getString(name) ?: error("No $name found in args")

typealias BuzzPattern = LongArray

fun Vibrator?.buzz(pattern: BuzzPattern): Unit =
    if (this == null) Timber.i("Vibrator is not available")
    else {
        // Use deprecated API to be compatible with old android API levels
        @Suppress("DEPRECATION")
        vibrate(pattern, -1)
    }
