package ru.spbstu.amd.learnbraille.database

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase

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
    LanguageConverter::class,
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
                "braille_lessons_database"
            )
            .addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    ioThread {
                        getInstance(context).apply {
                            symbolDao.insertSymbols(PREPOPULATE_LETTERS)
                            userDao.insertUser(DEFAULT_USER)
                        }

                        // TODO insert lessons
                        // TODO insert steps
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }
}
