package com.github.braillesystems.learnbraille.data.dsl

import com.github.braillesystems.learnbraille.data.entities.Course
import com.github.braillesystems.learnbraille.utils.side


/**
 * Provided values have no ID
 */
@DataBuilderMarker
class CoursesBuilder(block: CoursesBuilder.() -> Unit) {

    private val _data = mutableMapOf<Course, List<LessonWithSteps>>()
    internal val data: Map<Course, List<LessonWithSteps>>
        get() = _data

    init {
        block()
    }

    fun course(name: String, description: String, block: LessonAccumulatorBuilder.() -> Unit) =
        LessonAccumulatorBuilder(block).side {
            val course = Course(DEFAULT_ID, name, description)
            _data[course] = it.lessons
        }

    fun course(name: String, description: String, lessons: LessonsBuilder) {
        val course = Course(DEFAULT_ID, name, description)
        _data[course] = lessons.lessons
    }
}

@DataBuilderMarker
class LessonAccumulatorBuilder(block: LessonAccumulatorBuilder.() -> Unit) {

    private val _lessons = mutableListOf<LessonWithSteps>()
    internal val lessons: List<LessonWithSteps>
        get() = _lessons

    init {
        block()
    }

    operator fun LessonsBuilder.unaryPlus() {
        this@LessonAccumulatorBuilder._lessons += this.lessons
    }
}
