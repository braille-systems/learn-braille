package com.github.braillesystems.learnbraille.utils

import android.content.Context
import android.os.Vibrator
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.LearnBrailleApplication
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.android.get
import timber.log.Timber

val Fragment.application: LearnBrailleApplication
    get() = requireNotNull(activity).application as LearnBrailleApplication

fun scope(job: Job = Job()) = CoroutineScope(Dispatchers.Main + job)

fun Vibrator?.checkedBuzz(pattern: BuzzPattern, preferenceRepository: PreferenceRepository) {
    if (preferenceRepository.buzzEnabled) buzz(pattern)
}

fun checkedToast(msg: String, context: Context?, preferenceRepository: PreferenceRepository) {
    if (preferenceRepository.toastsEnabled) {
        Toast.makeText(
            context, msg, preferenceRepository.toastDuration
        ).show()
    }
}

fun Fragment.checkedToast(msg: String, preferenceRepository: PreferenceRepository = get()) =
    checkedToast(msg, context, preferenceRepository)

fun Fragment.toast(msg: String, preferenceRepository: PreferenceRepository = get()) =
    Toast.makeText(
        context, msg, preferenceRepository.toastDuration
    ).show()

fun Context.announceByAccessibility(
    announcement: String,
    preferenceRepository: PreferenceRepository
) {
    if (!preferenceRepository.announcementsEnabled) {
        Timber.i("Announcements disabled")
        return
    }

    val manager = accessibilityManager ?: return
    val event = AccessibilityEvent.obtain().apply {
        eventType = AccessibilityEvent.TYPE_ANNOUNCEMENT
        className = javaClass.name
        packageName = packageName
        text.add(announcement.removeHtmlMarkup())
    }
    manager.sendAccessibilityEvent(event)
}

fun Fragment.announceByAccessibility(
    announcement: String,
    preferenceRepository: PreferenceRepository = get()
) = application.announceByAccessibility(announcement, preferenceRepository)
