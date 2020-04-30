package com.github.braillesystems.learnbraille.data.db

import android.annotation.SuppressLint
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.res.russian.PREPOPULATE_LESSONS
import com.github.braillesystems.learnbraille.res.russian.PREPOPULATE_USERS
import com.github.braillesystems.learnbraille.res.russian.steps.PREPOPULATE_STEPS
import com.github.braillesystems.learnbraille.res.russian.symbols.PREPOPULATE_SYMBOLS
import com.github.braillesystems.learnbraille.userId
import com.github.braillesystems.learnbraille.utils.application
import com.github.braillesystems.learnbraille.utils.scope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

@Database(
    entities =
    [
        User::class, Lesson::class, Step::class, Symbol::class,
        UserKnowsSymbol::class, UserPassedStep::class, UserLastStep::class
    ],
    version = 6,
    exportSchema = false
)
@TypeConverters(
    BrailleDotsConverters::class,
    LanguageConverters::class,
    StepDataConverters::class
)
abstract class LearnBrailleDatabase : RoomDatabase() {

    abstract val userDao: UserDao
    abstract val lessonDao: LessonDao
    abstract val stepDao: StepDao
    abstract val symbolDao: SymbolDao
    abstract val userKnowsSymbolDao: UserKnowsSymbolDao
    abstract val userPassedStepDao: UserPassedStepDao
    abstract val userLastStep: UserLastStepDao

    companion object {

        const val name = "braille_lessons_database"

        @Volatile
        var prepopulationFinished = true
            private set

        @Volatile
        private var INSTANCE: LearnBrailleDatabase? = null

        @SuppressLint("SyntheticAccessor")
        fun getInstance(context: Context): LearnBrailleDatabase =
            INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                        ?: buildDatabase(
                            context
                        )
                            .also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) = Room
            .databaseBuilder(
                context.applicationContext,
                LearnBrailleDatabase::class.java,
                name
            )
            .addCallback(object : Callback() {

                @SuppressLint("SyntheticAccessor")
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Timber.d("onCreate")
                    prepopulate()
                }

                override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                    super.onDestructiveMigration(db)
                    Timber.i("onDestructiveMigration")
                    prepopulate()
                }

                private fun prepopulate() {
                    Timber.i("prepopulate")
                    prepopulationFinished = false
                    scope().launch {
                        Timber.i("Start database prepopulation")
                        getInstance(
                            context
                        ).apply {
                            userDao.insertUsers(PREPOPULATE_USERS)
                            lessonDao.insertLessons(PREPOPULATE_LESSONS)
                            stepDao.insertSteps(PREPOPULATE_STEPS)
                            symbolDao.insertSymbols(PREPOPULATE_SYMBOLS)
                        }
                        Timber.i("Finnish database prepopulation")
                        prepopulationFinished = true
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()

        private lateinit var forcePrepopulationJob: Job

        fun forcePrepopulation(context: Context) {
            forcePrepopulationJob = scope().launch {
                if (getInstance(context).userDao.getUser(context.userId) != null) {
                    Timber.i("DB has been already initialized")
                } else {
                    Timber.i("DB is not been initialized yet")
                }
            }
        }

        val isPrepopulated: Boolean
            get() = (forcePrepopulationJob.isCompleted && prepopulationFinished).also {
                if (it) Timber.i("DB has been prepopulated")
                else Timber.i(
                    "DB has not been prepopulated: " +
                            "forcePrepopulationJob.isCompleted = ${forcePrepopulationJob.isCompleted}, " +
                            "prepopulationFinished = $prepopulationFinished"
                )
            }
    }
}

fun Fragment.getDBInstance() = LearnBrailleDatabase.getInstance(application)
