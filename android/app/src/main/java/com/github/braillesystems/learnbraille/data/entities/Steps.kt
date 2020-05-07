package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*
import kotlinx.serialization.Serializable


@Entity(tableName = "steps")
@Serializable
data class Step(
    @PrimaryKey val id: Long,
    val data: StepData,
    @ColumnInfo(name = "lesson_id")
    val lessonId: Long
)

@Dao
interface StepDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(steps: List<Step>)

    @Query("select * from steps where id = :id")
    suspend fun getStep(id: Long): Step?

    @Query(
        """
        select steps.* from current_step as cs
        inner join steps on steps.id = step_id
        where cs.user_id = :userId and cs.course_id = :courseId
        """
    )
    suspend fun getCurrentStep(userId: Long, courseId: Long): Step?

    @Query(
        """
        select steps.* from last_course_step as ls
        inner join steps on steps.id = step_id
        where ls.user_id = :userId and ls.course_id = :courseId
        """
    )
    suspend fun getLastStep(userId: Long, courseId: Long): Step?

    @Query(
        """
        select steps.* from steps
        inner join lessons on lesson_id = lessons.id
        where lessons.course_id = :courseId
        order by steps.id limit 1
        """
    )
    suspend fun getFirstCourseStep(courseId: Long): Step?

    @Query(
        """
        select steps.* from steps
        inner join lessons on lessons.id = lesson_id
        where course_id = :courseId 
        and steps.id = :thisStepId + 1
        and exists(
            select * from current_step as cs
            where cs.user_id = :userId
            and cs.course_id = :courseId
            and cs.step_id > :thisStepId
        )
        """
    )
    suspend fun getNextStep(userId: Long, courseId: Long, thisStepId: Long): Step?

    @Query(
        """
        select steps.* from steps
        inner join lessons on lessons.id = lesson_id
        where course_id = :courseId
        and steps.id = :thisStepId - 1
        """
    )
    suspend fun getPrevStep(courseId: Long, thisStepId: Long): Step?
}
