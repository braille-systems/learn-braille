package ru.spbstu.amd.learnbraille.database

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.spbstu.amd.learnbraille.res.russian.PREPOPULATE_LESSONS
import ru.spbstu.amd.learnbraille.res.russian.PREPOPULATE_USERS
import ru.spbstu.amd.learnbraille.res.russian.steps.PREPOPULATE_STEPS
import ru.spbstu.amd.learnbraille.res.russian.symbols.PREPOPULATE_SYMBOLS

@Database(
    entities =
    [
        User::class, Lesson::class, Step::class, Symbol::class,
        UserKnowsSymbol::class, UserPassedStep::class
    ],
    version = 1,
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

    companion object {

        const val name = "braille_lessons_database"

        @Volatile
        private var INSTANCE: LearnBrailleDatabase? = null

        @SuppressLint("SyntheticAccessor")
        fun getInstance(context: Context): LearnBrailleDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                LearnBrailleDatabase::class.java,
                name
            )
            .addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    ioThread {
                        getInstance(context).apply {
                            userDao.insertUsers(PREPOPULATE_USERS)
                            lessonDao.insertLessons(PREPOPULATE_LESSONS)
                            stepDao.insertSteps(PREPOPULATE_STEPS)
                            symbolDao.insertSymbols(PREPOPULATE_SYMBOLS)
                        }
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }
}
