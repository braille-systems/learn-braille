package com.github.braillesystems.learnbraille.data.dsl

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.E
import com.github.braillesystems.learnbraille.data.entities.BrailleDot.F
import com.github.braillesystems.learnbraille.res.DeckTags
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


private val content by materials {
    +ruSymbols
    +enSymbols
    // ...
}

private val ruSymbols by symbols(symbolType = "ru") {
    symbol(char = 'А', brailleDots = BrailleDots(F, E, E, E, E, E))
    symbol(char = 'Б', brailleDots = BrailleDots(F, F, E, E, E, E))
    // ...
}

private val enSymbols by symbols(symbolType = "en") {
    symbol(char = 'Z', brailleDots = BrailleDots(F, F, F, E, E, E))
    // ...
}


val knownMaterials by known()


private val prepopulationData by data(
    materials = content,
    stepAnnotationNames = listOf("book", "bomb"),
    knownMaterials = knownMaterials
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
        deck(DeckTags.all) { true }
        // ...
    }
}

private val courseLessons by lessons {

    lesson(
        name = "Wow"
    ) {
        +FirstInfo("first")
        addSomeSteps()
    }
}

/*
 * Do not forget that builders is a regular Kotlin code, no magic.
 */
private fun StepsBuilder.addSomeSteps() {
    +Info("with book").annotate("book")
    +LastInfo("last")
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
        for (material in getMaterials(
            'Б',
            'Z'
        )) {
            +Input(material)
        }
        +LastInfo(InfoInterpolation.run {
            "LastInfo with stepId: $iStep, lessonId: $iLesson, courseName: $courseName"
        })
    }
}

private fun getMaterials(vararg chars: Char) =
    chars.map(content.symbols::getValue)


// Lists for testing

private val users = listOf(
    // Zero ID's because of autoincrement
    User(0, "default1", "John Smith"),
    User(0, "default2", ""),
    User(0, "default3", "")
)
private val mats = listOf(
    Material(1, Symbol(char = 'А', brailleDots = BrailleDots(F, E, E, E, E, E), type = "ru")),
    Material(2, Symbol(char = 'Б', brailleDots = BrailleDots(F, F, E, E, E, E), type = "ru")),
    Material(3, Symbol(char = 'Z', brailleDots = BrailleDots(F, F, F, E, E, E), type = "en"))
)
private val decks = listOf(
    Deck(2, "Ru letters"),
    Deck(3, "En letters"),
    Deck(1, DeckTags.all)
)
private val cards = listOf(
    Card(2, 1),
    Card(2, 2),
    Card(3, 3),
    Card(1, 1),
    Card(1, 2),
    Card(1, 3)
)
private val courses = listOf(
    Course(1, "Best course", "The best"),
    Course(2, "Worst course", "The worst")
)
private val lessons = listOf(
    Lesson(1, 1, "Wow", ""),
    Lesson(1, 2, "1", "")
)
private val steps = listOf(
    Step(1, 1, 1, FirstInfo("first")),
    Step(2, 1, 1, Info("with book")),
    Step(3, 1, 1, LastInfo("last")),

    Step(1, 2, 1, FirstInfo("FirstInfo")),
    Step(2, 2, 1, Info("Open your book and boom your bomb")),
    Step(
        3, 2, 1,
        ShowDots(
            text = "Перед Вами полное шеститочие",
            dots = BrailleDots(F, F, F, F, F, F)
        )
    ),
    Step(
        4, 2, 1,
        InputDots(
            text = "Введите все шесть точек",
            dots = BrailleDots(F, F, F, F, F, F)
        )
    ),
    Step(5, 2, 1, Show(content.symbols.getValue('Z'))),
    Step(6, 2, 1, Input(content.symbols.getValue('Б'))),
    Step(7, 2, 1, Input(content.symbols.getValue('Z'))),
    Step(
        8, 2, 1,
        LastInfo("LastInfo with stepId: 8, lessonId: 1, courseName: Worst course")
    )
)
private val stepAnnotations = listOf(
    StepAnnotation(1, "book"),
    StepAnnotation(2, "bomb")
)
private val stepHasAnnotations = listOf(
    StepHasAnnotation(1, 1, 2, 1),
    StepHasAnnotation(2, 1, 2, 1),
    StepHasAnnotation(2, 1, 2, 2)
)


@RunWith(AndroidJUnit4::class)
class DslTest {

    private lateinit var data: DataStorage

    @Before
    fun getBuilder() {
        data = prepopulationData
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
        assertEquals(stepAnnotations, data.stepAnnotations)
    }

    @Test
    fun testStepAnnotations() {
        assertEquals(stepHasAnnotations, data.stepsHasAnnotations)
    }
}
