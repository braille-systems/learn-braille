package com.github.braillesystems.learnbraille.data.dsl

import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.data.entities.Annotation
import com.github.braillesystems.learnbraille.utils.side
import kotlin.reflect.KProperty


const val DEFAULT_ID = -1L

typealias AnnotationName = String
typealias StepWithAnnotation = Pair<Step, AnnotationName?>
typealias LessonWithSteps = Pair<Lesson, List<StepWithAnnotation>>


class data(
    private val materials: _Materials,
    private val block: _Data.() -> Unit
) {

    internal operator fun getValue(thisRef: Any?, property: KProperty<*>): _DataWrapper {
        require(property.name == "prepopulationData") {
            "This value is used to prepopulate database, do not change it's name"
        }
        val data = _Data(materials, block)
        return _DataWrapper(data)
    }
}

class _DataWrapper(private val data: _Data) {

    fun use(block: _Data.() -> Unit) = data.block()
}

class _Data(
    private val _materials: _Materials,
    block: _Data.() -> Unit
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

    fun users(block: _Users.() -> Unit) {
        _users += _Users(block).users
    }

    fun courses(block: _Courses.() -> Unit) = _Courses(block).apply {
        val annotationByName = mutableMapOf<AnnotationName, Annotation>()

        annotations.forEach { annotation ->
            annotation.copy(id = _annotations.size + 1L).also {
                _annotations += it
                require(!annotationByName.contains(annotation.name)) {
                    "There should be only one annotation with name = ${annotation.name}"
                }
                annotationByName[annotation.name] = it
            }
        }

        data.forEach { (course, lessons) ->
            require(lessons.first().second.first().first.data is FirstInfo) {
                "First step of the course (${course.name}) should be FirstInfo"
            }
            // TODO require LastInfo
//            require(lessons.last().second.first().first.data is LastInfo) {
//                "Last step of the course (${course.name}) should be LastInfo"
//            }

            val courseId = courses.size + 1L
            _courses += course.copy(id = courseId)

            lessons.forEach { (lesson, steps) ->
                val lessonId = lessons.size + 1L
                _lessons += lesson.copy(id = lessonId, courseId = courseId)

                steps.forEach { (step, annotationName) ->
                    val stepId = _steps.size + 1L
                    _steps += step.copy(id = stepId, lessonId = lessonId)
                    if (annotationName != null) {
                        _stepAnnotations += StepAnnotation(
                            stepId, annotationByName[annotationName]?.id
                                ?: error("Step annotated with not existing annotation: $annotationName")
                        )
                    }
                }
            }
        }
    }

    fun decks(block: _Decks.() -> Unit) =
        _Decks(block).side {
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


class _Decks(block: _Decks.() -> Unit) {

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
