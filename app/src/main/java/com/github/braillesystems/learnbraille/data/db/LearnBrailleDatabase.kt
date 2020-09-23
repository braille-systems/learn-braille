package com.github.braillesystems.learnbraille.data.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.res.prepopulationData
import com.github.braillesystems.learnbraille.utils.DateConverters
import com.github.braillesystems.learnbraille.utils.devnull
import com.github.braillesystems.learnbraille.utils.logged
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
        Course::class, Lesson::class, Step::class, StepAnnotation::class, StepHasAnnotation::class,
        CurrentStep::class, LastCourseStep::class, LastLessonStep::class,
        Action::class
    ],
    version = 18,
    exportSchema = true
)
@TypeConverters(
    BrailleDotsConverters::class,
    MaterialDataTypeConverters::class,
    StepDataConverters::class,
    ActionTypeConverters::class,
    DateConverters::class
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
    abstract val stepAnnotationDao: StepAnnotationDao
    abstract val stepHasAnnotationDao: StepHasAnnotationDao

    abstract val currentStepDao: CurrentStepDao
    abstract val lastCourseStepDao: LastCourseStepDao
    abstract val lastLessonStepDao: LastLessonStepDao

    abstract val actionDao: ActionDao

    @Volatile
    private lateinit var prepareDbJob: Job

    /**
     * Android Room prepopulation and migrations are lazy,
     * they will start with the first request, blocking it.
     */
    private fun init(): LearnBrailleDatabase = this.also {
        prepareDbJob = scope().launch {
            Timber.i("Requesting value from database to force database callbacks and migrations")
            Timber.i("Start database preparation")
            userDao.user(1).devnull
            Timber.i("Finnish database preparation")
        }
    }

    val isInitialized: Boolean by logged {
        prepareDbJob.isCompleted
    }

    companion object {

        const val name = "learn_braille_database"

        /**
         * Try to run `buildDatabase` before first user's request (mb in Application's `onCreate`)
         * to make DB likely prepared until it is really needed.
         */
        fun buildDatabase(context: Context) = Room
            .databaseBuilder(
                context.applicationContext,
                LearnBrailleDatabase::class.java,
                name
            )
            .addCallback(object : Callback(), KoinComponent {

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
                    Timber.i("Prepopulate DB")
                    get<LearnBrailleDatabase>().apply {
                        scope(prepareDbJob).launch {
                            prepopulationData.run {
                                users?.let { userDao.insert(it) }
                                materials?.let { materialDao.insert(it) }
                                decks?.let { deckDao.insert(it) }
                                cards?.let { cardDao.insert(it) }
                                courses?.let { courseDao.insert(it) }
                                lessons?.let { lessonDao.insert(it) }
                                steps?.let { stepDao.insert(it) }
                                stepAnnotations?.let { stepAnnotationDao.insert(it) }
                                stepsHasAnnotations?.let { stepHasAnnotationDao.insert(it) }
                                knownMaterials?.let { knownMaterialDao.insert(it) }
                            }
                        }
                    }
                }
            })
            .addMigrations(
                MIGRATION_16_17,
                MIGRATION_17_18
            )
            .build()
            .init()
    }
}

private val MIGRATION_16_17 = object : Migration(16, 17), KoinComponent {
    override fun migrate(database: SupportSQLiteDatabase) {
        Timber.i("Start 16-17 migration")

        database.execSQL("delete from materials")
        database.execSQL("delete from steps")
        database.execSQL("delete from step_has_annotations")

        Timber.i("Removed old content")

        prepopulationData.run {
            materials?.forEach {
                database.insert(
                    "materials",
                    SQLiteDatabase.CONFLICT_IGNORE,
                    it.run {
                        ContentValues().apply {
                            put("id", id)
                            put("data", MaterialDataTypeConverters().to(data))
                        }
                    }
                )
            }
            Timber.i("Materials loaded")

            steps?.forEach {
                database.insert(
                    "steps",
                    SQLiteDatabase.CONFLICT_IGNORE,
                    it.run {
                        ContentValues().apply {
                            put("id", id)
                            put("course_id", courseId)
                            put("lesson_id", lessonId)
                            put("data", StepDataConverters().to(data))
                        }
                    }
                )
            }
            Timber.i("Steps loaded")

            stepsHasAnnotations?.forEach {
                database.insert(
                    "step_has_annotations",
                    SQLiteDatabase.CONFLICT_IGNORE,
                    it.run {
                        ContentValues().apply {
                            put("course_id", courseId)
                            put("lesson_id", lessonId)
                            put("step_id", stepId)
                            put("annotation_id", annotationId)
                        }
                    }
                )
            }
            Timber.i("Steps-annotations mapping loaded")
        }
    }
}

private val MIGRATION_17_18 = object : Migration(17, 18), KoinComponent {
    override fun migrate(database: SupportSQLiteDatabase) {
        Timber.i("Start 17-18 migration")
        database.execSQL(Action.creationQuery)
        Timber.i("Actions table created")
    }
}

// TODO migration that preserves current true user's position
