package com.github.braillesystems.learnbraille.data.db

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.braillesystems.learnbraille.data.dsl.Data
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
     * Run init every time application starts, after initializing DI
     * (migrations use it to get DB instance).
     *
     * Android Room prepopulation and migrations are lazy,
     * they will start with the first request, blocking it.
     * So a user will wait without any feedback if DB preparation takes some time.
     *
     * The better way is to call `init` before first request and do preparations asynchronously
     * and check DB preparation manually before requesting some data first time.
     */
    fun init(): LearnBrailleDatabase = this.also {
        prepareDbJob = scope().launch {
            Timber.i("Request value from database to force database callbacks and migrations")
            userDao.getUser(1).devnull
        }
    }

    val isInitialized: Boolean by logged {
        prepareDbJob.isCompleted
    }

    private fun prepare(block: suspend LearnBrailleDatabase.() -> Unit) {
        scope(prepareDbJob).launch {
            Timber.i("Start database preparation")
            this@LearnBrailleDatabase.block()
            Timber.i("Finnish database preparation")
        }
    }

    private suspend fun populateAll(data: Data) = populateAll16(data)

    private suspend fun populateAll16(data: Data) {
        data.apply {
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

    private suspend fun clearAll() = clearAll16()

    private suspend fun clearAll16() {
        userDao.clear()
        materialDao.clear()
        deckDao.clear()
        cardDao.clear()
        courseDao.clear()
        lessonDao.clear()
        stepDao.clear()
        stepAnnotationDao.clear()
        stepHasAnnotationDao.clear()
        knownMaterialDao.clear()
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
                    val db = get<LearnBrailleDatabase>()
                    db.prepare { populateAll(prepopulationData) }
                }
            })
            .addMigrations(
                contentUpdateMigration(16, 17),
                object : Migration(17, 18), KoinComponent {
                    override fun migrate(database: SupportSQLiteDatabase) =
                        get<LearnBrailleDatabase>().prepare {
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
            .fallbackToDestructiveMigration()
            .build()

        private fun contentUpdateMigration(fromVersion: Int, toVersion: Int) =
            contentUpdateMigration16(fromVersion, toVersion)

        private fun contentUpdateMigration16(fromVersion: Int, toVersion: Int) =
            object : Migration(fromVersion, toVersion), KoinComponent {
                override fun migrate(database: SupportSQLiteDatabase) =
                    get<LearnBrailleDatabase>().prepare {
                        clearAll16()
                        populateAll16(prepopulationData)
                    }
            }
    }
}
