package com.github.braillesystems.learnbraille.data.db

import android.annotation.SuppressLint
import android.content.Context
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
import kotlinx.coroutines.runBlocking
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
     * Run init every time application starts, after initializing DI
     * (migrations use it to get DB instance).
     *
     * Android Room prepopulation and migrations are lazy,
     * they will start with the first request, blocking it.
     *
     * TODO add reference to docs
     */
    fun init(): LearnBrailleDatabase = this.also {
        prepareDbJob = scope().launch {
            Timber.i("Requesting value from database to force database callbacks and migrations")
            Timber.i("Start database preparation")
            userDao.getUser(1).devnull
            Timber.i("Finnish database preparation")
        }
    }

    val isInitialized: Boolean by logged {
        prepareDbJob.isCompleted
    }

    companion object {

        const val name = "learn_braille_database"

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
                    runBlocking {
                        get<LearnBrailleDatabase>().apply {
                            prepopulationData.apply {
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
                object : Migration(16, 17), KoinComponent {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        // TODO 16-17 migration
                    }
                },
                object : Migration(17, 18), KoinComponent {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL(
                            """
                                CREATE TABLE actions (
                                    id   INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,
                                    type TEXT     NOT NULL,
                                    date INTEGER  NOT NULL
                                )
                            """.trimIndent()
                        )
                    }
                }
            )
            .build()
    }
}
