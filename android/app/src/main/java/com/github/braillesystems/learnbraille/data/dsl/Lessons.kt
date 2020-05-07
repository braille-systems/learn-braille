package com.github.braillesystems.learnbraille.data.dsl

import com.github.braillesystems.learnbraille.data.entities.Lesson
import com.github.braillesystems.learnbraille.data.entities.Step
import com.github.braillesystems.learnbraille.data.entities.StepData
import com.github.braillesystems.learnbraille.utils.side
import kotlin.reflect.KProperty

class lessons(private val block: LessonsBuilder.() -> Unit) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>) = LessonsBuilder(block)
}

@DataBuilderMarker
class LessonsBuilder(block: LessonsBuilder.() -> Unit) {

    private val _lessons = mutableListOf<LessonWithSteps>()
    internal val lessons: List<LessonWithSteps>
        get() = _lessons

    init {
        block()
    }

    fun lesson(name: String, description: String = "", block: StepsBuilder.() -> Unit) =
        StepsBuilder(block).side {
            _lessons += Pair(
                Lesson(DEFAULT_ID, name, description, DEFAULT_ID),
                it.steps
            )
        }
}

@DataBuilderMarker
class StepsBuilder(block: StepsBuilder.() -> Unit) {

    private val _steps = mutableListOf<StepWithAnnotations>()
    internal val steps: List<StepWithAnnotations>
        get() = _steps

    init {
        block()
    }

    operator fun StepData.unaryPlus() {
        _steps += Pair(
            Step(DEFAULT_ID, this, DEFAULT_ID),
            listOf()
        )
    }

    operator fun Pair<StepData, List<AnnotationName>>.unaryPlus() {
        _steps += Pair(
            Step(DEFAULT_ID, first, DEFAULT_ID),
            second
        )
    }
}

fun StepData.annotate(vararg names: AnnotationName) = Pair(this, names.toList())
