package com.github.braillesystems.learnbraille.data.dsl

import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.data.entities.Annotation
import com.github.braillesystems.learnbraille.utils.side
import kotlin.reflect.KProperty


const val DEFAULT_ID = -1L

typealias AnnotationName = String
typealias StepWithAnnotations = Pair<Step, List<AnnotationName>>
typealias LessonWithSteps = Pair<Lesson, List<StepWithAnnotations>>


@DslMarker
annotation class DataBuilderMarker


class data(
    private val materials: MaterialsBuilder,
    private val block: DataBuilder.() -> Unit
) {

    internal operator fun getValue(thisRef: Any?, property: KProperty<*>): DataWrapper {
        require(property.name == "prepopulationData") {
            "This value is used to prepopulate database, do not change it's name"
        }
        val data = DataBuilder(materials, block)
        return DataWrapper(data)
    }
}

class DataWrapper(private val data: DataBuilder) {

    fun use(block: DataBuilder.() -> Unit) = data.block()
}


@DataBuilderMarker
class DataBuilder(
    private val _materials: MaterialsBuilder,
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

    private val _annotations = mutableListOf<Annotation>()
    internal val annotations: List<Annotation>
        get() = _annotations

    private val _stepAnnotations = mutableListOf<StepAnnotation>()
    internal val stepAnnotations: List<StepAnnotation>
        get() = _stepAnnotations

    init {
        block()
    }

    fun users(block: UsersBuilder.() -> Unit) {
        _users += UsersBuilder(block).users
    }

    fun courses(block: CoursesBuilder.() -> Unit) {
        val coursesBuilder = CoursesBuilder(block)

        val annotationByName = mutableMapOf<AnnotationName, Annotation>()
        coursesBuilder.annotations.forEach { annotation ->
            annotation.copy(id = _annotations.size + 1L).also {
                _annotations += it
                require(!annotationByName.contains(it.name)) {
                    "There should be only one annotation with name = ${it.name}"
                }
                annotationByName[annotation.name] = it
            }
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

            lessonsWithSteps.forEach { (lesson, stepsWithAnnotations) ->
                val lessonId = _lessons.size + 1L
                _lessons += lesson.copy(id = lessonId, courseId = courseId)

                stepsWithAnnotations.forEach { (step, annotationNames) ->
                    val stepId = _steps.size + 1L
                    _steps += step.copy(id = stepId, lessonId = lessonId)

                    annotationNames.forEach {
                        val annotation = annotationByName[it]?.id
                            ?: error("Step annotated with not existing annotation: $annotationNames")
                        _stepAnnotations += StepAnnotation(stepId, annotation)
                    }
                }
            }
        }
    }

    fun decks(block: DecksBuilder.() -> Unit) =
        DecksBuilder(block).side {
            it.deckToPredicate.forEach { (deck, p) ->
                val deckId = decks.size + 1L
                _decks += deck.copy(id = deckId)

                materials
                    .filter { material -> p(material.data) }
                    .forEach { material ->
                        _cards += Card(deckId, material.id)
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

    fun deck(name: String, description: String = "", entryCriterion: (MaterialData) -> Boolean) {
        val deck = Deck(DEFAULT_ID, name, description)
        _deckToPredicate[deck] = entryCriterion
    }
}
