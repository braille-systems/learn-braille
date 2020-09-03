package com.github.braillesystems.learnbraille.data.repository

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.braillesystems.learnbraille.data.db.LearnBrailleDatabase
import com.github.braillesystems.learnbraille.data.entities.Action
import com.github.braillesystems.learnbraille.data.entities.PracticeHintAction
import com.github.braillesystems.learnbraille.data.entities.TheoryPassStep
import com.github.braillesystems.learnbraille.utils.Days
import com.github.braillesystems.learnbraille.utils.minus
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class ActionsRepositoryTest {

    private lateinit var db: LearnBrailleDatabase
    private lateinit var repo: MutableActionsRepository
    private val currDate = Date()
    private val actions = arrayOf(
        Action(id = 1, type = TheoryPassStep(isInput = false)),
        Action(id = 2, type = PracticeHintAction, date = currDate - Days(50))
    )

    @Before
    fun createDB() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room
            .inMemoryDatabaseBuilder(context, LearnBrailleDatabase::class.java)
            .allowMainThreadQueries()
            .build().apply {
                runBlocking {
                    actionDao.insert(*actions)
                }
            }
        repo = ActionsRepositoryImpl(
            db.actionDao,
            getCurrDate = { currDate },
            keepActionsTime = Days(100)
        )
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }

    @Test
    fun getAll() = runBlocking {
        assertEquals(actions.toList(), repo.getActionsFrom(Days(100)))
    }
}
