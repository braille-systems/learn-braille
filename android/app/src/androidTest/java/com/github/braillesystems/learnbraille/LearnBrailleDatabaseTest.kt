package com.github.braillesystems.learnbraille

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.braillesystems.learnbraille.data.db.LearnBrailleDatabase
import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.data.entities.Annotation
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.E
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.F
import com.github.braillesystems.learnbraille.res.SymbolType
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


/**
 * Tests data serialization first of all.
 */
@RunWith(AndroidJUnit4::class)
class LearnBrailleDatabaseTest {

    private lateinit var db: LearnBrailleDatabase

    private val users = listOf(
        User(
            login = "default",
            name = "John Smith"
        )
    )
    private val materials = listOf(
        Material(
            1, Symbol(
                symbol = 'А',
                brailleDots = BrailleDots(F, E, E, E, E, E),
                type = SymbolType.ru
            )
        )
    )
    private val decks = listOf(
        Deck(
            id = 1,
            name = "Ru letters"
        )
    )
    private val cards = listOf(
        Card(
            deckId = 1,
            materialId = 1
        )
    )
    private val courses = listOf(
        Course(
            id = 1,
            name = "Super course",
            description = "Oh, it's so good"
        )
    )
    private val lessons = listOf(
        Lesson(
            id = 1,
            name = "First",
            description = "First First First",
            courseId = 1
        ),
        Lesson(
            id = 2,
            name = "Last",
            description = "Last Last Last",
            courseId = 1
        )
    )
    private val steps = listOf(
        Step(
            id = 1,
            data = FirstInfo("FirstInfo"),
            lessonId = 1
        ),
        Step(
            id = 2,
            data = Info("Open your book"),
            lessonId = 1
        ),
        Step(
            id = 3,
            data = ShowDots(
                text = "Перед Вами полное шеститочие",
                dots = BrailleDots(F, F, F, F, F, F)
            ),
            lessonId = 1
        ),
        Step(
            id = 4,
            data = InputDots(
                text = "Введите все шесть точек",
                dots = BrailleDots(F, F, F, F, F, F)
            ),
            lessonId = 2
        ),
        Step(
            id = 5,
            data = Show(materials.first()),
            lessonId = 2
        ),
        Step(
            id = 6,
            data = Input(materials.first()),
            lessonId = 2
        ),
        Step(
            id = 7,
            data = LastInfo("LastInfo"),
            lessonId = 2
        )
    )
    private val annotations = listOf(
        Annotation(id = 1, name = "a1"),
        Annotation(id = 2, name = "a2")
    )
    private val stepAnnotations = listOf(
        StepAnnotation(
            stepId = 2,
            annotationId = 1
        )
    )

    @Before
    fun createDB() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room
            .inMemoryDatabaseBuilder(context, LearnBrailleDatabase::class.java)
            .allowMainThreadQueries()
            .build().apply {
                runBlocking {
                    userDao.insert(users)
                    materialDao.insert(materials)
                    deckDao.insert(decks)
                    cardDao.insert(cards)
                    courseDao.insert(courses)
                    lessonDao.insert(lessons)
                    stepDao.insert(steps)
                    annotationDao.insert(annotations)
                    stepAnnotationDao.insert(stepAnnotations)
                }
            }
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }

    @Test
    fun testUsers() = runBlocking {
        assertEquals("default", db.userDao.getUser(1)!!.login)
    }

    @Test
    fun testMaterials() = runBlocking {
        val data = db.materialDao.getMaterial(1)!!.data
        require(data is Symbol)
        assertEquals(BrailleDots(F, E, E, E, E, E), data.brailleDots)
    }

    @Test
    fun testDecks() = runBlocking {
        assertEquals("Ru letters", db.deckDao.getDeck(1)!!.name)
    }

    @Test
    fun testCourses() = runBlocking {
        assertEquals("Super course", db.courseDao.getCourse(1)!!.name)
    }

    @Test
    fun testSteps() = runBlocking {
        for ((i, step) in steps.withIndex()) {
            val fromDb = db.stepDao.getStep(i + 1L)!!
            assertEquals(step, fromDb)
        }
    }
}
