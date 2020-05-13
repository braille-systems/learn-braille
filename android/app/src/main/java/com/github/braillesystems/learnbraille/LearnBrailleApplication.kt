package com.github.braillesystems.learnbraille

import android.app.Application
import com.github.braillesystems.learnbraille.data.db.LearnBrailleDatabase
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.repository.MutablePracticeRepository
import com.github.braillesystems.learnbraille.data.repository.PracticeRepositoryImpl
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepositoryImpl
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
            factory<MutablePracticeRepository> {
                PracticeRepositoryImpl(get<LearnBrailleDatabase>().materialDao)
            }
            factory<PreferenceRepository> {
                PreferenceRepositoryImpl(
                    this@LearnBrailleApplication,
                    get<LearnBrailleDatabase>().userDao
                )
            }
//            factory<StepRepository> { (thisCourseId: Long) ->
//                StepRepositoryImpl(
//                    thisCourseId, get(),
//                    get<LearnBrailleDatabase>().stepDao,
//                    get<LearnBrailleDatabase>().currentStepDao,
//                    get<LearnBrailleDatabase>().lastCourseStepDao
//                )
//            }
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
