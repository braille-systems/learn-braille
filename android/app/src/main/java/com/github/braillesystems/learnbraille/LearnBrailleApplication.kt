package com.github.braillesystems.learnbraille

import android.app.Application
import android.content.Context
import android.os.Vibrator
import android.widget.Toast
import androidx.fragment.app.Fragment
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
            factory<StepRepository> { (thisCourseId: Long) ->
                StepRepositoryImpl(
                    thisCourseId, get(),
                    get<LearnBrailleDatabase>().stepDao,
                    get<LearnBrailleDatabase>().currentStepDao,
                    get<LearnBrailleDatabase>().lastCourseStepDao
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

fun Vibrator?.checkedBuzz(pattern: BuzzPattern, preferenceRepository: PreferenceRepository) {
    if (preferenceRepository.buzzEnabled) buzz(pattern)
}

fun toast(msg: String, context: Context?, preferenceRepository: PreferenceRepository) {
    if (preferenceRepository.toastsEnabled) {
        Toast.makeText(
            context, msg, preferenceRepository.toastDuration
        ).show()
    }
}

fun Fragment.toast(msg: String, preferenceRepository: PreferenceRepository = get()) =
    toast(msg, context, preferenceRepository)
