package com.github.braillesystems.learnbraille

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.braillesystems.learnbraille.data.dsl.*
import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.data.entities.Annotation
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.E
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.F
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


private val content by materials {
    +ruSymbols
    +enSymbols
    // ...
}

private val ruSymbols by symbols(type = "ru") {
    symbol(symbol = 'А', brailleDots = BrailleDots(F, E, E, E, E, E))
    symbol(symbol = 'Б', brailleDots = BrailleDots(F, F, E, E, E, E))
    // ...
}

private val enSymbols by symbols(type = "en") {
    symbol(symbol = 'Z', brailleDots = BrailleDots(F, F, F, E, E, E))
    // ...
}


private val prepopulationData by data(
    materials = content
) {

    users {
        user("default1", "John Smith")
    }

    users {
        user("default2", "")
        user("default3", "")
        // ...
    }

    courses {
        course(
            name = "Best course",
            description = "The best"
        ) {
            +courseLessons
            // ...
        }

        course(
            name = "Worst course",
            description = "The worst",
            lessons = worstLessons
        )

        annotations {
            +"book"
            +"bomb"
            // ...
        }
    }

    decks {
        deck("Ru letters") {
            it is Symbol && it.type == "ru"
        }
        // ...
    }

    decks {
        deck("En letters") {
            it is Symbol && it.type == "en"
        }
        // ...
    }
}

private val courseLessons by lessons {

    lesson(
        name = "Wow"
    ) {
        +FirstInfo("first")
        +Info("with book").annotate("book")
        +LastInfo("last")
    }
}

private val worstLessons by lessons {

    lesson("1") {
        +FirstInfo("FirstInfo")
        +Info("Open your book and boom your bomb").annotate("book", "bomb")
        +ShowDots(
            text = "Перед Вами полное шеститочие",
            dots = BrailleDots(F, F, F, F, F, F)
        )
        +InputDots(
            text = "Введите все шесть точек",
            dots = BrailleDots(F, F, F, F, F, F)
        )
        +Show(content.symbols.getValue('Z'))
        +Input(content.symbols.getValue('Z'))
        +LastInfo("LastInfo")
    }
}


// Lists for testing

private val users = listOf(
    // Zero ID's because of autoincrement
    User(0, "default1", "John Smith"),
    User(0, "default2", ""),
    User(0, "default3", "")
)
private val mats = listOf(
    Material(1, Symbol(symbol = 'А', brailleDots = BrailleDots(F, E, E, E, E, E), type = "ru")),
    Material(2, Symbol(symbol = 'Б', brailleDots = BrailleDots(F, F, E, E, E, E), type = "ru")),
    Material(3, Symbol(symbol = 'Z', brailleDots = BrailleDots(F, F, F, E, E, E), type = "en"))
)
private val decks = listOf(
    Deck(1, "Ru letters"),
    Deck(2, "En letters")
)
private val cards = listOf(
    Card(1, 1),
    Card(1, 2),
    Card(2, 3)
)
private val courses = listOf(
    Course(1, "Best course", "The best"),
    Course(2, "Worst course", "The worst")
)
private val lessons = listOf(
    Lesson(1, "Wow", "", 1),
    Lesson(2, "1", "", 2)
)
private val steps = listOf(
    Step(1, FirstInfo("first"), 1),
    Step(2, Info("with book"), 1),
    Step(3, LastInfo("last"), 1),

    Step(4, FirstInfo("FirstInfo"), 2),
    Step(5, Info("Open your book and boom your bomb"), 2),
    Step(
        6, ShowDots(
            text = "Перед Вами полное шеститочие",
            dots = BrailleDots(F, F, F, F, F, F)
        ), 2
    ),
    Step(
        7, InputDots(
            text = "Введите все шесть точек",
            dots = BrailleDots(F, F, F, F, F, F)
        ), 2
    ),
    Step(8, Show(content.symbols.getValue('Z')), 2),
    Step(9, Input(content.symbols.getValue('Z')), 2),
    Step(10, LastInfo("LastInfo"), 2)
)
private val annotations = listOf(
    Annotation(1, "book"),
    Annotation(2, "bomb")
)
private val stepAnnotations = listOf(
    StepAnnotation(2, 1),
    StepAnnotation(5, 1),
    StepAnnotation(5, 2)
)


@RunWith(AndroidJUnit4::class)
class DslTest {

    private lateinit var data: DataBuilder

    @Before
    fun getBuilder() {
        prepopulationData.use {
            data = this
        }
    }

    @Test
    fun testUsers() {
        assertEquals(users, data.users)
    }

    @Test
    fun testMaterials() {
        assertEquals(mats, data.materials)
    }

    @Test
    fun testDecks() {
        assertEquals(decks, data.decks)
    }

    @Test
    fun testCards() {
        assertEquals(cards, data.cards)
    }

    @Test
    fun testCourses() {
        assertEquals(courses, data.courses)
    }

    @Test
    fun testLessons() {
        assertEquals(lessons, data.lessons)
    }

    @Test
    fun testSteps() {
        assertEquals(steps, data.steps)
    }

    @Test
    fun testAnnotations() {
        assertEquals(annotations, data.annotations)
    }

    @Test
    fun testStepAnnotations() {
        assertEquals(stepAnnotations, data.stepAnnotations)
    }
}
