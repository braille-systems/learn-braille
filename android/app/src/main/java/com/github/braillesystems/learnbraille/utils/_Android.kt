package com.github.braillesystems.learnbraille.utils

/**
 * The file contains suitable extension functions for Android Framework
 * that are not specific for particular project.
 */

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.usb.UsbManager
import android.net.Uri
import android.os.Vibrator
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.view.accessibility.AccessibilityManager
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.github.braillesystems.learnbraille.R
import timber.log.Timber


val Context.usbManager get() = getSystemService(Context.USB_SERVICE) as UsbManager
val Context.accessibilityManager: AccessibilityManager?
    get() =
        if (!isAccessibilityEnabled) null
        else getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager

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

val Context.isAccessibilityEnabled: Boolean by logged {
    try {
        Settings.Secure.getInt(
            contentResolver,
            Settings.Secure.ACCESSIBILITY_ENABLED
        ) == 1
    } catch (e: SettingNotFoundException) {
        Timber.e(e)
        false
    }
}

/**
 * Fixes multiple navigation issue: `IllegalArgumentException x is unknown to this NavController`
 * https://blog.jakelee.co.uk/resolving-crash-illegalargumentexception-x-is-unknown-to-this-navcontroller/
 */
fun Fragment.navigate(id: Int): Unit = try {
    findNavController().navigate(id)
} catch (e: IllegalArgumentException) {
    Timber.e("Multitouch navigation", e)
}

/**
 * Fixes multiple navigation issue: `IllegalArgumentException x is unknown to this NavController`
 * https://blog.jakelee.co.uk/resolving-crash-illegalargumentexception-x-is-unknown-to-this-navcontroller/
 */
fun Fragment.navigate(action: NavDirections) = try {
    findNavController().navigate(action)
} catch (e: IllegalArgumentException) {
    Timber.e("Multitouch navigation", e)
}

val Context.appName: String by lazyWithContext { getString(R.string.app_name) }
val Fragment.appName: String
    get() = context?.appName ?: error("Fragment is expected to have a context")

val Context.preferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)
