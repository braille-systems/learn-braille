package com.github.braillesystems.learnbraille.data.db

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.data.entities.Annotation
import com.github.braillesystems.learnbraille.res.prepopulationData
import com.github.braillesystems.learnbraille.utils.scope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get
import timber.log.Timber

@Database(
    entities =
    [
        User::class, Material::class, KnownMaterial::class,
        Deck::class, Card::class,
        Course::class, Lesson::class, Step::class, Annotation::class, StepAnnotation::class,
        CurrentStep::class, LastCourseStep::class
    ],
    version = 10,
    exportSchema = false
)
@TypeConverters(
    BrailleDotsConverters::class,
    MaterialDataTypeConverters::class, StepDataConverters::class
)
abstract class LearnBrailleDatabase : RoomDatabase(), KoinComponent {

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

    abstract val currentStepDao: CurrentStepDao
    abstract val lastCourseStepDao: LastCourseStepDao

    private lateinit var forcePrepopulationJob: Job

    @Volatile
    private var prepopulationFinished = true

    fun init(): LearnBrailleDatabase = this.also {
        forcePrepopulationJob = scope().launch {
            // Correct if at least one user is always prepopulated
            if (userDao.getUser(1) != null) {
                Timber.i("DB has been already initialized")
            } else {
                Timber.i("DB is not been initialized yet")
                prepopulationFinished = false
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

    companion object {

        const val name = "learn_braille_database"

        fun buildDatabase(context: Context) = Room
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
                    object : KoinComponent {
                        fun runPrepopulation() {
                            get<LearnBrailleDatabase>().apply {
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
                    }.runPrepopulation()
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }
}
