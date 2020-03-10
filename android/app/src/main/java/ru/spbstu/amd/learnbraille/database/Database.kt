package ru.spbstu.amd.learnbraille.database

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Symbol::class], version = 1, exportSchema = false)
@TypeConverters(BrailleDotsConverters::class, LanguageConverter::class)
abstract class LearnBrailleDatabase : RoomDatabase() {

    abstract val symbolDao: SymbolDao

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
                        getInstance(context).symbolDao.insertLetters(PREPOPULATE_LETTERS)
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }
}


