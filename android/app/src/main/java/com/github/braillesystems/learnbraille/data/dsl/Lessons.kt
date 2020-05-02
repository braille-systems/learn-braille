package com.github.braillesystems.learnbraille.data.dsl

import com.github.braillesystems.learnbraille.data.entities.Lesson
import com.github.braillesystems.learnbraille.data.entities.Step
import com.github.braillesystems.learnbraille.data.entities.StepData
import com.github.braillesystems.learnbraille.utils.side
import kotlin.reflect.KProperty

class lessons(private val block: _Lessons.() -> Unit) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>) = _Lessons(block)
}

class _Lessons(block: _Lessons.() -> Unit) {

    private val _lessons = mutableListOf<LessonWithSteps>()
    internal val lessons: List<LessonWithSteps>
        get() = _lessons

    init {
        block()
    }

    fun lesson(name: String, description: String = "", block: _Steps.() -> Unit) =
        _Steps(block).side {
            _lessons += Pair(
                Lesson(DEFAULT_ID, name, description, DEFAULT_ID),
                it.steps
            )
        }
}

class _Steps(block: _Steps.() -> Unit) {

    private val _steps = mutableListOf<StepWithAnnotation>()
    internal val steps: List<StepWithAnnotation>
        get() = TODO()

    init {
        block()
    }

    operator fun StepData.unaryPlus() {
        _steps += Pair(
            Step(DEFAULT_ID, this, DEFAULT_ID),
            null
        )
    }

    operator fun Pair<StepData, AnnotationName>.unaryPlus() {
        _steps += Pair(
            Step(DEFAULT_ID, first, DEFAULT_ID),
            second
        )
    }
}

infix fun StepData.annotate(name: AnnotationName) = Pair(this, name)
