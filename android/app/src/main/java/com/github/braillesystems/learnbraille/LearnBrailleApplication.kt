package com.github.braillesystems.learnbraille

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Vibrator
import android.widget.Toast
import com.github.braillesystems.learnbraille.data.db.LearnBrailleDatabase
import com.github.braillesystems.learnbraille.utils.BuzzPattern
import com.github.braillesystems.learnbraille.utils.buzz
import timber.log.Timber

class LearnBrailleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("onCreate")

        // TODO move behind repository abstraction barrier
        LearnBrailleDatabase.init(this)

        USE_DEBUG_LESSONS = preferences.getBoolean(
            getString(R.string.preference_use_debug_lessons), false
        )
    }
}

val Context.preferences: SharedPreferences
    get() = getSharedPreferences(
        getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )

val CORRECT_BUZZ_PATTERN: BuzzPattern = longArrayOf(100, 100, 100, 100, 100, 100)
val INCORRECT_BUZZ_PATTERN: BuzzPattern = longArrayOf(0, 200)

fun Vibrator?.checkedBuzz(context: Context, pattern: BuzzPattern) {
    val buzzEnabled = context.preferences.getBoolean(
        context.getString(R.string.preference_enable_buzz), true
    )
    if (buzzEnabled) buzz(pattern)
}

const val TOAST_DURATION = Toast.LENGTH_SHORT

/**
 * Restart app to update
 */
var USE_DEBUG_LESSONS: Boolean = false
    private set

val Context.userId: Long
    get() = preferences.getInt(getString(R.string.preference_current_user), 1).toLong()
