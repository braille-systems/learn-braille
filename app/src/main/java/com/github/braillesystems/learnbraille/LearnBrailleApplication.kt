package com.github.braillesystems.learnbraille

import android.app.Application
import com.github.braillesystems.learnbraille.data.db.LearnBrailleDatabase
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.repository.*
import com.github.braillesystems.learnbraille.ui.screens.practice.CardViewModelFactory
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class LearnBrailleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("onCreate")

        val koinModule = module {
            single { LearnBrailleDatabase.buildDatabase(this@LearnBrailleApplication) }
            factory<PreferenceRepository> {
                PreferenceRepositoryImpl(
                    this@LearnBrailleApplication,
                    get<LearnBrailleDatabase>().userDao
                )
            }
            factory<MutablePreferenceRepository> {
                PreferenceRepositoryImpl(
                    this@LearnBrailleApplication,
                    get<LearnBrailleDatabase>().userDao
                )
            }
            factory<PracticeRepository> {
                val db = get<LearnBrailleDatabase>()
                PracticeRepositoryImpl(
                    this@LearnBrailleApplication,
                    db.deckDao, db.cardDao, get()
                )
            }
            factory<MutablePracticeRepository> {
                val db = get<LearnBrailleDatabase>()
                PracticeRepositoryImpl(
                    this@LearnBrailleApplication,
                    db.deckDao, db.cardDao, get()
                )
            }

            factory<BrowserRepository> {
                val db = get<LearnBrailleDatabase>()
                BrowserRepositoryImpl(
                    this@LearnBrailleApplication,
                    get(), db.deckDao, db.cardDao
                )
            }
            factory<MutableBrowserRepository> {
                val db = get<LearnBrailleDatabase>()
                BrowserRepositoryImpl(
                    this@LearnBrailleApplication,
                    get(), db.deckDao, db.cardDao
                )
            }

            factory<TheoryRepository> {
                get<LearnBrailleDatabase>().run {
                    TheoryRepositoryImpl(
                        lessonDao, stepDao,
                        currentStepDao, lastCourseStepDao, lastLessonStepDao, knownMaterialDao,
                        get()
                    )
                }
            }
            factory<MutableTheoryRepository> {
                get<LearnBrailleDatabase>().run {
                    TheoryRepositoryImpl(
                        lessonDao, stepDao,
                        currentStepDao, lastCourseStepDao, lastLessonStepDao, knownMaterialDao,
                        get()
                    )
                }
            }
            factory { (getEnteredDots: () -> BrailleDots) ->
                CardViewModelFactory(
                    get(),
                    this@LearnBrailleApplication,
                    getEnteredDots
                )
            }
        }
        startKoin {
            androidContext(this@LearnBrailleApplication)
            modules(koinModule)
        }

        get<LearnBrailleDatabase>().init()
    }
}

/**
 * First always stands for test developers course
 */
const val COURSE_ID = 2L
