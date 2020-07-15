package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*


@Entity(
    tableName = "step_has_annotations",
    primaryKeys = ["course_id", "lesson_id", "step_id", "annotation_id"]
)
data class StepHasAnnotation(
    @ColumnInfo(name = "course_id")
    val courseId: Long,
    @ColumnInfo(name = "lesson_id")
    val lessonId: Long,
    @ColumnInfo(name = "step_id")
    val stepId: Long,
    @ColumnInfo(name = "annotation_id")
    val annotationId: Long
)

@Dao
interface StepHasAnnotationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: List<StepHasAnnotation>)
}
