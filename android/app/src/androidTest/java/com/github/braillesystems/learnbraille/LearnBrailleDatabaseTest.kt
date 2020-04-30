package com.github.braillesystems.learnbraille

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.braillesystems.learnbraille.data.db.LearnBrailleDatabase
import com.github.braillesystems.learnbraille.data.types.StepDao
import com.github.braillesystems.learnbraille.data.types.SymbolDao
import com.github.braillesystems.learnbraille.data.types.UserDao
import com.github.braillesystems.learnbraille.data.types.UserPassedStepDao
import com.github.braillesystems.learnbraille.res.russian.PREPOPULATE_USERS
import com.github.braillesystems.learnbraille.res.russian.steps.DEBUG_LESSONS
import com.github.braillesystems.learnbraille.res.russian.symbols.PREPOPULATE_SYMBOLS
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
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
