package com.github.braillesystems.learnbraille

import android.app.Application
import android.os.Vibrator
import com.github.braillesystems.learnbraille.data.db.LearnBrailleDatabase
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.repository.*
import com.github.braillesystems.learnbraille.ui.screens.practice.CardViewModelFactory
import com.github.braillesystems.learnbraille.utils.BuzzPattern
import com.github.braillesystems.learnbraille.utils.buzz
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
            factory<CardRepository> { CardRepositoryImpl(get<LearnBrailleDatabase>().cardDao) }
            factory<PreferenceRepository> {
                PreferenceRepositoryImpl(
                    this@LearnBrailleApplication,
                    get<LearnBrailleDatabase>().userDao
                )
            }
            factory<StepRepository> {
                StepRepositoryImpl(
                    get(), get<LearnBrailleDatabase>().stepDao
                )
            }
            factory<UserRepository> { UserRepositoryImpl(get<LearnBrailleDatabase>().userDao) }
            factory { (getEnteredDots: () -> BrailleDots) ->
                CardViewModelFactory(
                    get(), get(),
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

fun Vibrator?.checkedBuzz(preferenceRepository: PreferenceRepository, pattern: BuzzPattern) {
    if (preferenceRepository.buzzEnabled) buzz(pattern)
}
