package com.github.braillesystems.learnbraille.data.dsl

import com.github.braillesystems.learnbraille.data.entities.Course
import com.github.braillesystems.learnbraille.data.entities.CourseDesc
import com.github.braillesystems.learnbraille.data.entities.CourseName
import com.github.braillesystems.learnbraille.data.entities.DBid
import com.github.braillesystems.learnbraille.utils.side

sealed class CourseID(val id: DBid)

object DevelopersCourse : CourseID(1)

class UsersCourse(id: DBid) : CourseID(id) {
    init {
        require(id > 1) {
            "id == 1 stands for developers course"
        }
    }
}

@DataBuilderMarker
class CoursesBuilder(block: CoursesBuilder.() -> Unit) {

    private val _data = mutableMapOf<Course, List<LessonWithSteps>>()
    internal val data: Map<Course, List<LessonWithSteps>>
        get() = _data

    init {
        block()
    }

    fun course(
        name: CourseName, description: CourseDesc,
        block: LessonAccumulatorBuilder.() -> Unit
    ) = LessonAccumulatorBuilder(block).side {
        val course = Course(UNDEFINED_ID, name, description)
        _data[course] = it.lessons
    }

    fun course(name: CourseName, description: CourseDesc, lessons: LessonsBuilder) {
        val course = Course(UNDEFINED_ID, name, description)
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
