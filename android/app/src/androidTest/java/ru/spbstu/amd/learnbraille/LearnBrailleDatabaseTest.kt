package ru.spbstu.amd.learnbraille

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.spbstu.amd.learnbraille.database.*
import ru.spbstu.amd.learnbraille.database.BrailleDot.F
import ru.spbstu.amd.learnbraille.res.russian.PREPOPULATE_LESSONS
import ru.spbstu.amd.learnbraille.res.russian.PREPOPULATE_USERS
import ru.spbstu.amd.learnbraille.res.russian.steps.PREPOPULATE_STEPS
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
                userDao.insertUsers(PREPOPULATE_USERS)
                lessonDao.insertLessons(PREPOPULATE_LESSONS)
                stepDao.insertSteps(PREPOPULATE_STEPS)
                symbolDao.insertSymbols(PREPOPULATE_SYMBOLS)
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

    @Test
    @Throws(Exception::class)
    fun defaultUserIsThere() {
        assertEquals(1L, userDao.getId("default"))
    }

    @Test
    @Throws(Exception::class)
    fun ruLettersAreThere() {
        val letter = symbolDao.getSymbol('А')
        assertEquals('А', letter?.symbol)
    }

    @Test
    @Throws(Exception::class)
    fun stepOrder() {
        val (_, step1) = stepDao.getCurrentStepForUser(TEST_USER) ?: error("No first step")
        assertEquals(1, step1.id)
        assertTrue(step1.data is Info)
        userPassedStepDao.insertPassedStep(UserPassedStep(1, 1))

        val (_, step2) = stepDao.getCurrentStepForUser(TEST_USER) ?: error("No second step")
        assertEquals(2, step2.id)
        assertTrue(step2.data is ShowDots)
        assertEquals(BrailleDots(F, F, F, F, F, F), (step2.data as ShowDots).dots)
    }

    companion object {
        const val TEST_USER = 1L
    }
}
