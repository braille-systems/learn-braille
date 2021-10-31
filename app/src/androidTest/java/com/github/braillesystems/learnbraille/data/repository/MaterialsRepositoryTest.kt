package com.github.braillesystems.learnbraille.data.repository

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.braillesystems.learnbraille.data.UnreachablePreferencesRepository
import com.github.braillesystems.learnbraille.data.db.LearnBrailleDatabase
import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.data.entities.StepAnnotation
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.*
import com.github.braillesystems.learnbraille.res.*
import com.github.braillesystems.learnbraille.res.SymbolType
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MaterialsRepositoryTest {

    private lateinit var db: LearnBrailleDatabase
    private lateinit var repo: MaterialsRepository

    private val users = listOf(
        User(
            login = "default",
            name = "John Smith"
        )
    )

    private val materials = listOf(
        Material(
            1,
            Symbol(
                char = 'А',
                brailleDots = BrailleDots(F, E, E, E, E, E),
                type = SymbolType.ru
            )
        ),
        Material(
            2,
            MarkerSymbol(
                type = MarkerType.GreekCapital,
                brailleDots = BrailleDots(F, F, F, E, E, E)
            )
        ),
        Material(
            3,
            Symbol(
                char = 'B',
                brailleDots = BrailleDots(F, F, F, E, E, F),
                type = SymbolType.digit
            )
        )
    )

    private val knownMaterials = listOf(
        KnownMaterial(1, 2),
        KnownMaterial(1, 3)
    )

    private val decks = listOf(
        Deck(
            id = 1,
            tag = "Ru letters"
        ),
        Deck(
            id = 2,
            tag = "Another useless deck"
        )
    )

    private val cards = listOf(
        Card(
            deckId = 1,
            materialId = 1
        ),
        Card(
            deckId = 2,
            materialId = 1
        ),
        Card(
            deckId = 1,
            materialId = 2
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
            lessonId = 1, courseId = 1
        )
    )

    private val annotations = listOf(
        StepAnnotation(id = 1, name = "a1"),
        StepAnnotation(id = 2, name = "a2")
    )

    private val stepAnnotations = listOf(
        StepHasAnnotation(
            courseId = 1,
            lessonId = 3,
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
                    knownMaterialDao.insert(knownMaterials)
                    deckDao.insert(decks)
                    cardDao.insert(cards)
                    courseDao.insert(courses)
                    lessonDao.insert(lessons)
                    stepDao.insert(steps)
                    stepAnnotationDao.insert(annotations)
                    stepHasAnnotationDao.insert(stepAnnotations)
                }
            }

        repo = MaterialsRepositoryImpl(
            db.deckDao, db.cardDao,
            object : UnreachablePreferencesRepository() {
                override val practiceUseOnlyKnownMaterials: Boolean
                    get() = true
                override val currentUserId: DBid
                    get() = 1

                override suspend fun getCurrentUser(): User = users.first()
            }
        )
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }

    @Test
    fun randomMaterialFromDeck() = runBlocking {
        val material = repo.randomMaterialFromDeck(2)
        assertTrue(material in materials.filter { Card(2, it.id) in cards })
    }

    @Test
    fun randomKnownMaterialFromDeck() = runBlocking {
        val material = repo.randomKnownMaterialFromDeck(1)
        assertTrue(material in materials.filter { Card(1, it.id) in cards })
        assertTrue(material!!.id in knownMaterials.map { it.materialId })
    }

    @Test
    fun allMaterialsFromDeck() = runBlocking {
        assertEquals(
            materials.filter { Card(2, it.id) in cards },
            repo.allMaterialsFromDeck(2)
        )
    }

    @Test
    fun allDecks() = runBlocking {
        assertEquals(
            decks,
            repo.allDecks()
        )
    }

    @Test
    fun availableDecks() = runBlocking {
        assertEquals(listOf(decks.first()), repo.availableDecks())
    }

    @Test
    fun allDecksWithAvailability() = runBlocking {
        assertEquals(
            listOf(
                DeckWithAvailability(decks[0], true),
                DeckWithAvailability(decks[1], false)
            ),
            repo.allDecksWithAvailability()
        )
    }
}
