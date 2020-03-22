package ru.spbstu.amd.learnbraille.database

import androidx.room.*

@Entity(tableName = "step")
data class Step(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    val title: String,

    @ColumnInfo(name = "lesson_id")
    val lessonId: Long,

    val data: StepData
)

data class LessonNameWithStep(

    @ColumnInfo(name = "lesson_name")
    val lessonName: String,

    @Embedded
    val step: Step
)

@Dao
interface StepDao {

    @Insert
    fun insertSteps(steps: List<Step>)

    @Query(
        """
            SELECT lesson.name AS 'lesson_name', step.*
            FROM step
            INNER JOIN lesson on lesson_id = lesson.id
            WHERE NOT EXISTS (
                SELECT *
                FROM user_passed_step AS ups
                WHERE ups.user_id = :userId AND ups.step_id = step.id
            )
            ORDER BY step.id ASC
            LIMIT 1
            """
    )
    fun getCurrentStepForUser(userId: Long): LessonNameWithStep?
}
