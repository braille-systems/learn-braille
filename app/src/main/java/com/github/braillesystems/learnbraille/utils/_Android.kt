package com.github.braillesystems.learnbraille.utils

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
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.room.TypeConverter
import com.github.braillesystems.learnbraille.R
import timber.log.Timber
import java.util.*
import kotlin.reflect.KProperty

/**
 * The file contains suitable extension functions for Android Framework
 * that are not specific for particular project.
 */

val Context.usbManager get() = getSystemService(Context.USB_SERVICE) as UsbManager

val Context.accessibilityManager: AccessibilityManager?
    get() =
        if (!isAccessibilityEnabled) null
        else getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager

fun Fragment.getFragmentStringArg(name: String): String =
    arguments
        ?.getString(name)
        ?: error("No $name found in args")

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
    Timber.e(e, "Multitouch navigation")
}

/**
 * Fixes multiple navigation issue: `IllegalArgumentException x is unknown to this NavController`
 * https://blog.jakelee.co.uk/resolving-crash-illegalargumentexception-x-is-unknown-to-this-navcontroller/
 */
fun Fragment.navigate(action: NavDirections) = try {
    findNavController().navigate(action)
} catch (e: IllegalArgumentException) {
    Timber.e(e, "Multitouch navigation")
}

val Context.appName: String get() = getString(R.string.app_name)
val Fragment.appName: String
    get() = context?.appName ?: error("Fragment is expected to have a context")

val Context.preferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)

fun Button.setSize(width: Int? = null, height: Int? = null) {
    layoutParams = layoutParams.apply {
        width?.let { this.width = width }
        height?.let { this.height = height }
    }
}

class logged<C, T>(
    private val logger: (String) -> Unit = { Timber.d(it) },
    private val setter: (C.(T) -> Unit)? = null,
    private val getter: C.(KProperty<*>) -> T
) {

    operator fun getValue(thisRef: C, property: KProperty<*>): T =
        thisRef
            .getter(property)
            .also { logger("${property.name} -> $it") }

    operator fun setValue(thisRef: C, property: KProperty<*>, value: T) =
        setter
            ?.let { thisRef.it(value) }
            ?.also { logger("${property.name} <- $value") }
            ?: error("Setter is expected to be set")
}

class DateConverters {

    @TypeConverter
    fun to(d: Date): Long = d.time

    @TypeConverter
    fun from(value: Long): Date = Date(value)
}
