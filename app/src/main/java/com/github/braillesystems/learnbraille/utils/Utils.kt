package com.github.braillesystems.learnbraille.utils

import android.content.Context
import android.os.Vibrator
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.LearnBrailleApplication
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import org.koin.android.ext.android.get

/**
 * This file contains suitable extension functions for this project.
 */

val Fragment.application: LearnBrailleApplication
    get() = requireNotNull(activity).application as LearnBrailleApplication

fun scope(job: Job = Job()) = CoroutineScope(Dispatchers.Main + job)


fun Vibrator?.checkedBuzz(pattern: BuzzPattern, preferenceRepository: PreferenceRepository) =
    runIf(preferenceRepository.buzzEnabled) { buzz(pattern) }

fun checkedToast(msg: String, context: Context?, preferenceRepository: PreferenceRepository) =
    runIf(preferenceRepository.toastsEnabled) {
        if (GlobalToastState.repeatsLast(
                msg,
                preferenceRepository.toastDuration == Toast.LENGTH_SHORT
            )
        ) {
            return
        }
        Toast.makeText(context, msg, preferenceRepository.toastDuration).show()
    }

fun Fragment.checkedToast(msg: String, preferenceRepository: PreferenceRepository = get()) =
    checkedToast(msg, context, preferenceRepository)

class GlobalToastState {
    companion object {
        const val SHORT_DURATION_TIMEOUT: Long = 4000 // private in Toast.java
        const val LONG_DURATION_TIMEOUT: Long = 7000

        var lastToastMsg: String = ""
        var lastToastInvocationTime: Long = 0L

        fun repeatsLast(msg: String, isShort: Boolean): Boolean {
            val timeNow = System.currentTimeMillis()
            val toastDuration: Long = if (isShort) SHORT_DURATION_TIMEOUT else LONG_DURATION_TIMEOUT
            if (msg == lastToastMsg
                && lastToastInvocationTime + toastDuration > timeNow
            ) {
                return false
            }
            lastToastInvocationTime = timeNow
            lastToastMsg = msg
            return true
        }
    }
}

fun Fragment.toast(msg: String, preferenceRepository: PreferenceRepository = get()) {
    if (GlobalToastState.repeatsLast(
            msg,
            preferenceRepository.toastDuration == Toast.LENGTH_SHORT
        )
    ) {
        return
    }
    Toast.makeText(context, msg, preferenceRepository.toastDuration).show()
}


fun Context.announce(announcement: String) {
    val manager = accessibilityManager ?: return
    val event = AccessibilityEvent.obtain().apply {
        eventType = AccessibilityEvent.TYPE_ANNOUNCEMENT
        className = javaClass.name
        packageName = packageName
        text.add(announcement.removeHtmlMarkup())
    }
    manager.sendAccessibilityEvent(event)
}

fun Fragment.announce(announcement: String) =
    application.announce(announcement)

fun Fragment.checkedAnnounce(
    announcement: String,
    preferenceRepository: PreferenceRepository = get()
) = runIf(preferenceRepository.additionalAnnouncementsEnabled) {
    announce(announcement)
}


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


fun <T> stringify(s: SerializationStrategy<T>, obj: T) = Json.stringify(s, obj)
fun <T> parse(d: DeserializationStrategy<T>, s: String) = Json.parse(d, s)

val Context.extendedTextSize: Float by lazyWithContext {
    // Size applied in runtime is different
    resources.getDimension(R.dimen.lessons_info_text_size) / 5 * 3
}
