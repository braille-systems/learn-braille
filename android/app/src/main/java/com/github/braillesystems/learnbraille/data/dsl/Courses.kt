package com.github.braillesystems.learnbraille.data.dsl

import com.github.braillesystems.learnbraille.data.entities.Annotation
import com.github.braillesystems.learnbraille.data.entities.Course
import com.github.braillesystems.learnbraille.utils.side


/**
 * Provided values have no ID
 */
class _Courses(block: _Courses.() -> Unit) {

    private val _data = mutableMapOf<Course, List<LessonWithSteps>>()
    internal val data: Map<Course, List<LessonWithSteps>>
        get() = _data

    private val _annotations = mutableListOf<Annotation>()
    internal val annotations: List<Annotation>
        get() = _annotations

    init {
        block()
    }

    fun course(name: String, description: String, block: _LessonAccumulator.() -> Unit) =
        _LessonAccumulator(block).side {
            val course = Course(DEFAULT_ID, name, description)
            _data[course] = it.lessons
        }

    fun annotations(block: _Annotations.() -> Unit) =
        _Annotations(block).side {
            _annotations += it.annotations
        }
}

class _LessonAccumulator(block: _LessonAccumulator.() -> Unit) {

    private val _lessons = mutableListOf<LessonWithSteps>()
    internal val lessons: List<LessonWithSteps>
        get() = _lessons

    init {
        block()
    }

    operator fun _Lessons.unaryPlus() {
        _lessons += this.lessons
    }

    operator fun LessonWithSteps.unaryPlus() {

    }
}

class _Annotations(block: _Annotations.() -> Unit) {

    private val _annotations = mutableListOf<Annotation>()
    internal val annotations: List<Annotation>
        get() = _annotations

    init {
        block()
    }

    operator fun String.unaryPlus() {
        _annotations += Annotation(DEFAULT_ID, this)
    }
}
