package com.github.braillesystems.learnbraille

import android.app.Application
import com.github.braillesystems.learnbraille.database.LearnBrailleDatabase
import com.github.braillesystems.learnbraille.database.entities.Language
import com.github.braillesystems.learnbraille.util.scope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class LearnBrailleApplication : Application() {

    lateinit var prepopulationJob: Job
        private set

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("onCreate")

        // Start database prepopulation
        LearnBrailleDatabase.getInstance(this).apply {
            prepopulationJob = scope()
                .launch {
                    userDao.getUser(defaultUser)?.let {
                        Timber.i("DB has been already initialized")
                    } ?: Timber.i("DB is not initialized yet")
                }
        }
    }
}

typealias BuzzPattern = LongArray

val CORRECT_BUZZ_PATTERN: BuzzPattern = longArrayOf(100, 100, 100, 100, 100, 100)
val INCORRECT_BUZZ_PATTERN: BuzzPattern = longArrayOf(0, 200)

// TODO move to settings
val language = Language.RU
const val defaultUser = 1L

const val DEBUG = true
