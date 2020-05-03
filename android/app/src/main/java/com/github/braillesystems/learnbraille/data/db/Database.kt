package com.github.braillesystems.learnbraille.data.db

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.res.prepopulationData
import com.github.braillesystems.learnbraille.userId
import com.github.braillesystems.learnbraille.utils.scope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

@Database(
    entities =
    [
        User::class, Material::class, KnownMaterial::class,
        Deck::class, Card::class,
        Course::class, Lesson::class, Step::class, Annotation::class, StepAnnotation::class
    ],
    version = 6, // TODO increment version
    exportSchema = false
)
@TypeConverters(
    // TODO TypeConverters
)
abstract class LearnBrailleDatabase : RoomDatabase() {

    abstract val userDao: UserDao
    abstract val materialDao: MaterialDao
    abstract val knownMaterialDao: KnownMaterialDao

    abstract val deckDao: DeckDao
    abstract val cardDao: CardDao

    abstract val courseDao: CourseDao
    abstract val lessonDao: LessonDao
    abstract val stepDao: StepDao
    abstract val annotationDao: AnnotationsDao
    abstract val stepAnnotationDao: StepAnnotationDao


    companion object {

        const val name = "learn_braille_database"

        @Volatile
        private var prepopulationFinished = true

        @Volatile
        private var INSTANCE: LearnBrailleDatabase? = null

        @SuppressLint("SyntheticAccessor")
        fun getInstance(context: Context): LearnBrailleDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
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
                    Timber.i("Prepopulate")
                    prepopulationFinished = false
                    getInstance(context).apply {
                        prepopulationData.use {
                            scope().launch {
                                Timber.i("Start database prepopulation")

                                userDao.insert(users)
                                materialDao.insert(materials)
                                deckDao.insert(decks)
                                cardDao.insert(cards)
                                courseDao.insert(courses)
                                lessonDao.insert(lessons)
                                stepDao.insert(steps)
                                annotationDao.insert(annotations)
                                stepAnnotationDao.insert(stepAnnotations)

                                Timber.i("Finnish database prepopulation")
                                prepopulationFinished = true
                            }
                        }
                    }

                }
            })
            .fallbackToDestructiveMigration()
            .build()

        private lateinit var forcePrepopulationJob: Job

        fun init(context: Context) {
            forcePrepopulationJob = scope().launch {
                if (getInstance(context).userDao.getUser(context.userId) != null) {
                    Timber.i("DB has been already initialized")
                } else {
                    Timber.i("DB is not been initialized yet")
                }
            }
        }

        val isInitialized: Boolean
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
