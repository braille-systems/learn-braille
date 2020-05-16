package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*
import com.github.braillesystems.learnbraille.data.dsl.StepAnnotationName
import kotlinx.serialization.Serializable


@Entity(tableName = "steps", primaryKeys = ["id", "course_id", "lesson_id"])
@Serializable
data class Step(
    val id: Long,
    @ColumnInfo(name = "course_id")
    val courseId: Long,
    @ColumnInfo(name = "lesson_id")
    val lessonId: Long,
    val data: StepData
) : Comparable<Step> {

    override operator fun compareTo(other: Step): Int {
        require(other.courseId == courseId) {
            "Only steps of the same course are comparable"
        }
        if (this == other) return 0
        return if (lessonId < other.lessonId ||
            (lessonId == other.lessonId && id < other.id)
        ) {
            -1
        } else {
            1
        }
    }
}

/**
 * Be careful: there are duplications of some SQL code.
 */
@Dao
interface StepDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(steps: List<Step>)

    @Query("select * from steps where id = :id")
    suspend fun getStep(id: Long): Step?

    @Query(
        """
        select steps.* from current_step as cs
        inner join steps on steps.course_id = cs.course_id 
        and steps.lesson_id = cs.lesson_id and steps.id = cs.step_id 
        where cs.user_id = :userId and cs.course_id = :courseId
        """
    )
    suspend fun getCurrentStep(userId: Long, courseId: Long): Step?

    @Query(
        """
        select steps.* from last_course_step as ls
        inner join steps on steps.course_id = ls.course_id 
        and steps.lesson_id = ls.lesson_id and steps.id = ls.step_id
        where ls.user_id = :userId and ls.course_id = :courseId
        """
    )
    suspend fun getLastStep(userId: Long, courseId: Long): Step?

    @Query(
        """
        select steps.* from steps
        where steps.course_id = :courseId
        order by steps.lesson_id, steps.id limit 1
        """
    )
    suspend fun getFirstCourseStep(courseId: Long): Step?

    @Query(
        """
        select steps.* from steps
        where course_id = :courseId 
        and (
            (id > :thisStepId and lesson_id = :thisLessonId)
            or (lesson_id > :thisLessonId)
        )
        and not exists(
            select * from step_has_annotations as sha
            inner join step_annotations as sa on sha.annotation_id = sa.id
            where sha.course_id = :courseId 
            and sha.lesson_id = steps.lesson_id and sha.step_id = steps.id
            and sa.name in (:proscribedAnnotations)
        )
        order by lesson_id, id
        limit 1
        """
    )
    suspend fun getNextStep(
        courseId: Long,
        thisLessonId: Long,
        thisStepId: Long,
        proscribedAnnotations: List<StepAnnotationName> = listOf()
    ): Step?

    @Query(
        """
        select steps.* from steps
        where course_id = :courseId
        and (
            (id < :thisStepId and lesson_id = :thisLessonId)
            or (lesson_id < :thisLessonId)
        )
        and not exists(
            select * from step_has_annotations as sha
            inner join step_annotations as sa on sha.annotation_id = sa.id
            where sha.course_id = :courseId 
            and sha.lesson_id = steps.lesson_id and sha.step_id = steps.id
            and sa.name in (:proscribedAnnotations)
        )
        order by lesson_id desc, id desc
        limit 1
        """
    )
    suspend fun getPrevStep(
        courseId: Long,
        thisLessonId: Long,
        thisStepId: Long,
        proscribedAnnotations: List<StepAnnotationName> = listOf()
    ): Step?
}
