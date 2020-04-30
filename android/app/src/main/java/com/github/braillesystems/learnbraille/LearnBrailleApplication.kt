package com.github.braillesystems.learnbraille

import android.app.Application
import android.os.Vibrator
import android.widget.Toast
import com.github.braillesystems.learnbraille.data.db.LearnBrailleDatabase
import com.github.braillesystems.learnbraille.data.entities.Language
import com.github.braillesystems.learnbraille.utils.BuzzPattern
import com.github.braillesystems.learnbraille.utils.buzz
import timber.log.Timber

class LearnBrailleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("onCreate")

        // TODO move behind repository abstraction barrier
        LearnBrailleDatabase.forcePrepopulation(this)
    }
}

val CORRECT_BUZZ_PATTERN: BuzzPattern = longArrayOf(100, 100, 100, 100, 100, 100)
val INCORRECT_BUZZ_PATTERN: BuzzPattern = longArrayOf(0, 200)

fun Vibrator?.checkedBuzz(pattern: BuzzPattern): Unit =
    if (true) TODO("Check if enabled")
    else buzz(pattern)

val language: Language = TODO("move to settings")
val defaultUser: Long = TODO("move to settings")

const val TOAST_DURATION = Toast.LENGTH_SHORT

val USE_DEBUG_LESSONS: Boolean = TODO("Move to settings")
