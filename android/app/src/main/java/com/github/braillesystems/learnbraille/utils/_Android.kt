package com.github.braillesystems.learnbraille.utils

/**
 * The file contains suitable extension functions for Android Framework
 * that are not specific for particular project.
 */

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbManager
import android.net.Uri
import android.os.Vibrator
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import timber.log.Timber

val Context.usbManager get() = getSystemService(Context.USB_SERVICE) as UsbManager
val Context.accessibilityManager: AccessibilityManager?
    get() =
        if (!isAccessibilityEnabled) null
        else getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager

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

fun Fragment.sendMarketIntent(appPackageName: String) {
    fun start(prefix: String) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(prefix + appPackageName)
            )
        )
    }

    try {
        start("market://details?id=")
    } catch (e: ActivityNotFoundException) {
        start("https://play.google.com/store/apps/details?id=")
    }
}

val Context.isAccessibilityEnabled: Boolean
    get() = Settings.Secure.getInt(
        contentResolver,
        Settings.Secure.ACCESSIBILITY_ENABLED
    ) == 1
