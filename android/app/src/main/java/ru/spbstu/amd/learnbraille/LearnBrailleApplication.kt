package ru.spbstu.amd.learnbraille

import android.app.Application
import ru.spbstu.amd.learnbraille.database.entities.Language
import timber.log.Timber

class LearnBrailleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}

val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
val INCORRECT_BUZZ_PATTERN = longArrayOf(0, 200)

// TODO move to settings
val language = Language.RU
const val defaultUser = 1L
