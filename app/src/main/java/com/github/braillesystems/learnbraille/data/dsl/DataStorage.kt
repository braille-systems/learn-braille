package com.github.braillesystems.learnbraille.data.dsl

import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.res.DeckTags
import com.github.braillesystems.learnbraille.utils.side
import kotlin.reflect.KProperty

interface DataStorage {
    val users: List<User>?
    val materials: List<Material>?
    val decks: List<Deck>?
    val cards: List<Card>?
    val courses: List<Course>?
    val lessons: List<Lesson>?
    val steps: List<Step>?
    val stepAnnotations: List<StepAnnotation>?
    val stepsHasAnnotations: List<StepHasAnnotation>?
    val knownMaterials: List<KnownMaterial>?
}

typealias StepWithAnnotations = Pair<Step, List<StepAnnotationName>>
typealias LessonWithSteps = Pair<Lesson, List<StepWithAnnotations>>

class data(
    private val materials: MaterialsBuilder,
    private val stepAnnotationNames: List<StepAnnotationName>,
    private val knownMaterials: List<KnownMaterial>,
    private val block: DataBuilder.() -> Unit
) {
    internal operator fun getValue(thisRef: Any?, property: KProperty<*>): DataStorage =
        DataBuilder(materials, stepAnnotationNames, knownMaterials, block)
}

@DataBuilderMarker
class DataBuilder(
    materials: MaterialsBuilder,
    private val stepAnnotationNames: List<StepAnnotationName>,
    knownMaterials: List<KnownMaterial>,
    block: DataBuilder.() -> Unit
) : DataStorage {

    private val _users = mutableListOf<User>()
    override val users: List<User>
        get() = _users

    private val _materials: MaterialsBuilder = materials
    override val materials: List<Material>
        get() = _materials.materials

    private val _decks = mutableListOf<Deck>()
    override val decks: List<Deck>
        get() = _decks

    private val _cards = mutableListOf<Card>()
    override val cards: List<Card>
        get() = _cards

    private val _courses = mutableListOf<Course>()
    override val courses: List<Course>
        get() = _courses

    private val _lessons = mutableListOf<Lesson>()
    override val lessons: List<Lesson>
        get() = _lessons

    private val _steps = mutableListOf<Step>()
    override val steps: List<Step>
        get() = _steps

    private val _stepAnnotations = mutableListOf<StepAnnotation>()
    override val stepAnnotations: List<StepAnnotation>
        get() = _stepAnnotations

    private val _stepsHasAnnotations = mutableListOf<StepHasAnnotation>()
    override val stepsHasAnnotations: List<StepHasAnnotation>
        get() = _stepsHasAnnotations

    private val _knownMaterials: List<KnownMaterial> = knownMaterials
    override val knownMaterials: List<KnownMaterial> by lazy {
        _knownMaterials.map { it.copy(userId = 1) }
    }

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
                    _steps += step
                        .copy(id = stepId, courseId = courseId, lessonId = lessonId)
                        .interpolateText(course.name)

                    stepAnnotationNames.forEach {
                        val stepAnnotation = annotationByName[it]?.id
                            ?: error("Step annotated with not existing annotation: $it")
                        _stepsHasAnnotations += StepHasAnnotation(
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
                    if (deck.tag == DeckTags.Grouping.All.tag) ALL_CARDS_DECK_ID
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
        val deck = Deck(UNDEFINED_ID, tag)
        _deckToPredicate[deck] = entryCriterion
    }
}

private fun Step.interpolateText(courseName: CourseName): Step =
    this.copy(
        data =
        if (data !is BaseInfo) data
        else {
            when (data) {
                is FirstInfo -> FirstInfo(interpolateTextHelper(data.text, courseName))
                is LastInfo -> LastInfo(interpolateTextHelper(data.text, courseName))
                is Info -> Info(interpolateTextHelper(data.text, courseName))
            }
        }
    )

private fun Step.interpolateTextHelper(text: HtmlText, courseName: CourseName): HtmlText =
    text
        .replace(InfoInterpolation.iStep, id.toString())
        .replace(InfoInterpolation.iLesson, lessonId.toString())
        .replace(InfoInterpolation.courseName, courseName)
