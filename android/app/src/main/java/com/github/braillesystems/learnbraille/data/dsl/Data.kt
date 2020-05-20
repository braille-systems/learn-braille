package com.github.braillesystems.learnbraille.data.dsl

import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.res.DeckTags
import com.github.braillesystems.learnbraille.utils.side
import kotlin.reflect.KProperty


const val DEFAULT_ID = -1L
const val ALL_CARDS_DECK_ID = 1L

typealias StepAnnotationName = String
typealias StepWithAnnotations = Pair<Step, List<StepAnnotationName>>
typealias LessonWithSteps = Pair<Lesson, List<StepWithAnnotations>>


@DslMarker
annotation class DataBuilderMarker


class data(
    private val materials: MaterialsBuilder,
    private val stepAnnotations: List<StepAnnotationName>,
    private val block: DataBuilder.() -> Unit
) {

    internal operator fun getValue(thisRef: Any?, property: KProperty<*>): DataWrapper {
        require(property.name == "prepopulationData") {
            "This value is used to prepopulate database, do not change it's name"
        }
        val data = DataBuilder(materials, stepAnnotations, block)
        return DataWrapper(data)
    }
}

class DataWrapper(private val data: DataBuilder) {

    fun use(block: DataBuilder.() -> Unit) = data.block()
}


@DataBuilderMarker
class DataBuilder(
    private val _materials: MaterialsBuilder,
    private val stepAnnotationNames: List<StepAnnotationName>,
    block: DataBuilder.() -> Unit
) {

    private val _users = mutableListOf<User>()
    internal val users: List<User>
        get() = _users

    internal val materials: List<Material>
        get() = _materials.materials

    private val _decks = mutableListOf<Deck>()
    internal val decks: List<Deck>
        get() = _decks

    private val _cards = mutableListOf<Card>()
    internal val cards: List<Card>
        get() = _cards

    private val _courses = mutableListOf<Course>()
    internal val courses: List<Course>
        get() = _courses

    private val _lessons = mutableListOf<Lesson>()
    internal val lessons: List<Lesson>
        get() = _lessons

    private val _steps = mutableListOf<Step>()
    internal val steps: List<Step>
        get() = _steps

    private val _stepAnnotations = mutableListOf<StepAnnotation>()
    internal val stepAnnotations: List<StepAnnotation>
        get() = _stepAnnotations

    private val _stepHasAnnotations = mutableListOf<StepHasAnnotation>()
    internal val stepHasAnnotations: List<StepHasAnnotation>
        get() = _stepHasAnnotations

    init {
        block()
    }

    fun users(block: UsersBuilder.() -> Unit) {
        _users += UsersBuilder(block).users
    }

    fun courses(block: CoursesBuilder.() -> Unit) {
        val coursesBuilder = CoursesBuilder(block)

        val annotationByName = mutableMapOf<StepAnnotationName, StepAnnotation>()
        stepAnnotationNames.forEach { stepAnnotationName ->
            val stepAnnotation = StepAnnotation(_stepAnnotations.size + 1L, stepAnnotationName)
            _stepAnnotations += stepAnnotation
            require(!annotationByName.containsKey(stepAnnotationName)) {
                "Should be only one annotation with name = $stepAnnotationName"
            }
            annotationByName[stepAnnotationName] = stepAnnotation
        }

        coursesBuilder.data.forEach { (course, lessonsWithSteps) ->
            require(lessonsWithSteps.first().second.first().first.data is FirstInfo) {
                "First step of the course (${course.name}) should be FirstInfo"
            }
            require(lessonsWithSteps.last().second.last().first.data is LastInfo) {
                "Last step of the course (${course.name}) should be LastInfo"
            }

            val courseId = courses.size + 1L
            _courses += course.copy(id = courseId)

            lessonsWithSteps.forEachIndexed { iLesson, (lesson, stepsWithAnnotations) ->
                val lessonId = iLesson + 1L
                _lessons += lesson.copy(id = lessonId, courseId = courseId)

                stepsWithAnnotations.forEachIndexed { iStep, (step, stepAnnotationNames) ->
                    val stepId = iStep + 1L
                    _steps += step.copy(id = stepId, courseId = courseId, lessonId = lessonId)

                    stepAnnotationNames.forEach {
                        val stepAnnotation = annotationByName[it]?.id
                            ?: error("Step annotated with not existing annotation: $it")
                        _stepHasAnnotations += StepHasAnnotation(
                            courseId = courseId,
                            lessonId = lessonId,
                            stepId = stepId,
                            annotationId = stepAnnotation
                        )
                    }
                }
            }
        }
    }

    fun decks(block: DecksBuilder.() -> Unit) =
        DecksBuilder(block).side {
            it.deckToPredicate.forEach { (deck, p) ->
                val deckId =
                    if (deck.tag == DeckTags.all) ALL_CARDS_DECK_ID
                    else decks.size + 2L
                val deckMaterials = materials.filter { material -> p(material.data) }
                if (deckMaterials.isNotEmpty()) {
                    _decks += deck.copy(id = deckId)
                    _cards += deckMaterials.map { material ->
                        Card(deckId, material.id)
                    }
                }
            }
        }
}


@DataBuilderMarker
class DecksBuilder(block: DecksBuilder.() -> Unit) {

    private val _deckToPredicate = mutableMapOf<Deck, (MaterialData) -> Boolean>()
    internal val deckToPredicate: Map<Deck, (MaterialData) -> Boolean>
        get() = _deckToPredicate

    init {
        block()
    }

    fun deck(tag: String, entryCriterion: (MaterialData) -> Boolean) {
        val deck = Deck(DEFAULT_ID, tag)
        _deckToPredicate[deck] = entryCriterion
    }
}
