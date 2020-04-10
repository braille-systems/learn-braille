package ru.spbstu.amd.learnbraille.database.entities

import androidx.room.*
import ru.spbstu.amd.learnbraille.*

@Entity(tableName = "step")
data class Step(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    val title: String,

    @ColumnInfo(name = "lesson_id")
    val lessonId: Long,

    val data: StepData
) {
    companion object {
        val pattern = Regex(
            """Step\(id=(\d+), title=((?:.|\n)*), lessonId=(\d+), data=((?:.|\n)+)\)"""
        )
    }
}

fun stepOf(string: String) = Step.pattern.matchEntire(string)
    ?.groups?.let { (_, id, title, lessonId, data) ->
        Step(
            id = id?.value?.toLong() ?: error("No id here $string"),
            title = title?.value ?: error("No title here $string"),
            lessonId = lessonId?.value?.toLong() ?: error("No lessonId here $string"),
            data = stepDataOf(data?.value ?: error("No data here $string"))
        )
    } ?: error("$string does not match symbol structure")

@Dao
interface StepDao {

    @Insert
    fun insertSteps(steps: List<Step>)

    @Query(
        """
            SELECT * FROM step
            WHERE NOT EXISTS (
                SELECT * FROM user_passed_step AS ups
                WHERE ups.user_id = :userId AND ups.step_id = step.id
            )
            ORDER BY step.id ASC
            LIMIT 1
            """
    )
    fun getCurrentStepForUser(userId: Long): Step?

    @Query(
        """
            SELECT * FROM step
            WHERE EXISTS (
                SELECT * FROM user_passed_step AS ups
                WHERE ups.user_id = :userId AND ups.step_id = :stepId
            ) AND step.id > :stepId
            ORDER BY step.id ASC
            LIMIT 1
            """
    )
    fun getNextStepForUser(userId: Long, stepId: Long): Step?

    @Query("SELECT * FROM step WHERE step.id = :id")
    fun getStep(id: Long): Step?
}
