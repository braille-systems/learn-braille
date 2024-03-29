package com.github.braillesystems.learnbraille

import android.app.Application
import com.github.braillesystems.learnbraille.data.db.LearnBrailleDatabase
import com.github.braillesystems.learnbraille.data.dsl.UsersCourse
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.repository.*
import com.github.braillesystems.learnbraille.ui.screens.practice.CardViewModelFactory
import com.github.braillesystems.learnbraille.utils.devnull
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
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

            factory<ActionsRepository> {
                ActionsRepositoryImpl(get<LearnBrailleDatabase>().actionDao, get())
            }
            factory<MutableActionsRepository> {
                ActionsRepositoryImpl(get<LearnBrailleDatabase>().actionDao, get())
            }

            factory<MaterialsRepository> {
                val db = get<LearnBrailleDatabase>()
                MaterialsRepositoryImpl(db.deckDao, db.cardDao, get())
            }

            factory<PracticeRepository> {
                val db = get<LearnBrailleDatabase>()
                PracticeRepositoryImpl(
                    this@LearnBrailleApplication,
                    db.deckDao, get(), get()
                )
            }
            factory<MutablePracticeRepository> {
                val db = get<LearnBrailleDatabase>()
                PracticeRepositoryImpl(
                    this@LearnBrailleApplication,
                    db.deckDao, get(), get()
                )
            }

            factory<BrowserRepository> {
                BrowserRepositoryImpl(
                    this@LearnBrailleApplication,
                    get(), get()
                )
            }
            factory<MutableBrowserRepository> {
                BrowserRepositoryImpl(
                    this@LearnBrailleApplication,
                    get(), get()
                )
            }

            factory<TheoryRepository> {
                get<LearnBrailleDatabase>().run {
                    TheoryRepositoryImpl(
                        lessonDao, stepDao,
                        currentStepDao, lastCourseStepDao, lastLessonStepDao, knownMaterialDao,
                        get(), get()
                    )
                }
            }
            factory<MutableTheoryRepository> {
                get<LearnBrailleDatabase>().run {
                    TheoryRepositoryImpl(
                        lessonDao, stepDao,
                        currentStepDao, lastCourseStepDao, lastLessonStepDao, knownMaterialDao,
                        get(), get()
                    )
                }
            }

            factory { (getEnteredDots: () -> BrailleDots) ->
                CardViewModelFactory(
                    get(), get(), get(),
                    this@LearnBrailleApplication,
                    getEnteredDots
                )
            }
        }

        koin = startKoin {
            androidContext(this@LearnBrailleApplication)
            modules(koinModule)
        }.koin

        // Touch database to force it's preparation
        get<LearnBrailleDatabase>().devnull
    }
}

lateinit var koin: Koin
    private set

val COURSE = UsersCourse
