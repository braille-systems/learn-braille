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

data class LessonWithStep(

    @Embedded(prefix = "lesson_embedding_")
    val lesson: Lesson,

    @Embedded
    val step: Step
)

@Dao
interface StepDao {

    @Insert
    fun insertSteps(steps: List<Step>)

    @Query(
        """
            SELECT step.*, 
                lesson.id AS 'lesson_embedding_id', 
                lesson.name AS 'lesson_embedding_name'
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
    fun getCurrentStepForUser(userId: Long): LessonWithStep?

    @Query(
        """
            SELECT step.*, 
                lesson.id AS 'lesson_embedding_id', 
                lesson.name AS 'lesson_embedding_name'
            FROM step
            INNER JOIN lesson on lesson_id = lesson.id
            ORDER BY step.id ASC
            LIMIT 1
            """
    )
    fun getStep(id: Long): LessonWithStep?
}
