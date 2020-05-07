package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*


/**
 * `Current` means: last not passed.
 */
@Entity(tableName = "current_step", primaryKeys = ["user_id", "course_id"])
data class CurrentStep(
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "course_id")
    val courseId: Long,
    @ColumnInfo(name = "step_id")
    val stepId: Long
)

@Dao
interface CurrentStepDao {

    @Insert
    suspend fun insert(vararg currentSteps: CurrentStep)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(currentStep: CurrentStep)
}


/**
 * `Last` means: last seen.
 */
@Entity(tableName = "last_course_step", primaryKeys = ["user_id", "course_id"])
data class LastCourseStep(
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "course_id")
    val courseId: Long,
    @ColumnInfo(name = "step_id")
    val stepId: Long
)

@Dao
interface LastCourseStepDao {

    @Insert
    suspend fun insert(vararg lastCourseSteps: LastCourseStep)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(lastCourseStep: LastCourseStep)
}


/**
 * Last step, what user visited in lesson
 */
@Entity(tableName = "last_lesson_step", primaryKeys = ["user_id", "course_id", "lesson_id"])
data class LastLessonStep(
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "course_id")
    val courseId: Long,
    @ColumnInfo(name = "lesson_id")
    val lessonId: Long,
    @ColumnInfo(name = "step_id")
    val stepId: Long
)

@Dao
interface LastLessonStepDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(lastLessonStep: LastLessonStep)
}
