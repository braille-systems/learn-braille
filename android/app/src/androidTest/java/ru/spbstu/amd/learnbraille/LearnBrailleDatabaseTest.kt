package ru.spbstu.amd.learnbraille

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import ru.spbstu.amd.learnbraille.database.LearnBrailleDatabase
import ru.spbstu.amd.learnbraille.database.entities.StepDao
import ru.spbstu.amd.learnbraille.database.entities.SymbolDao
import ru.spbstu.amd.learnbraille.database.entities.UserDao
import ru.spbstu.amd.learnbraille.database.entities.UserPassedStepDao
import ru.spbstu.amd.learnbraille.res.russian.PREPOPULATE_USERS
import ru.spbstu.amd.learnbraille.res.russian.steps.DEBUG_LESSONS
import ru.spbstu.amd.learnbraille.res.russian.symbols.PREPOPULATE_SYMBOLS
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class LearnBrailleDatabaseTest {

    private lateinit var db: LearnBrailleDatabase
    private lateinit var userDao: UserDao
    private lateinit var stepDao: StepDao
    private lateinit var symbolDao: SymbolDao
    private lateinit var userPassedStepDao: UserPassedStepDao

    @Before
    fun createDB() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room
            .inMemoryDatabaseBuilder(context, LearnBrailleDatabase::class.java)
            .allowMainThreadQueries()
            .build().apply {
                runBlocking {
                    userDao.insertUsers(PREPOPULATE_USERS)
                    stepDao.insertSteps(DEBUG_LESSONS)
                    symbolDao.insertSymbols(PREPOPULATE_SYMBOLS)
                }
            }

        userDao = db.userDao
        stepDao = db.stepDao
        symbolDao = db.symbolDao
        userPassedStepDao = db.userPassedStepDao
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }

    // TODO actualize

    companion object {
        const val TEST_USER = 1L
    }
}
