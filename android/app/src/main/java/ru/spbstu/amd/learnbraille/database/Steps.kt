package ru.spbstu.amd.learnbraille.database

import androidx.room.*

@Entity(tableName = "step")
data class Step(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    val title: String,

    val text: String? = null,

    val symbol: Char? = null,

    @ColumnInfo(name = "braille_symbol")
    val brailleSymbol: BrailleDots? = null,

    @ColumnInfo(name = "lesson_id")
    val lessonId: Long,

    @ColumnInfo(name = "type_id")
    val typeId: Long
)

data class LessonWithStepWithStepType(

    @Embedded
    val lesson: Lesson,

    @Embedded
    val step: Step,

    @Embedded
    val stepType: StepType
)

interface StepDao {

    @Insert
    fun insertSteps(steps: List<Step>)

    @Query(
        """
            SELECT * 
            FROM step 
            INNER JOIN step_type on type_id = step_type.id 
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
    fun getCurrentStepForUser(userId: Long): LessonWithStepWithStepType?
}
