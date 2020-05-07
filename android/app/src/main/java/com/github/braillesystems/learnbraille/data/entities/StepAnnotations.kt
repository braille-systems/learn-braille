package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*


@Entity(tableName = "step_annotations", primaryKeys = ["step_id", "annotation_id"])
data class StepAnnotation(
    @ColumnInfo(name = "step_id")
    val stepId: Long,
    @ColumnInfo(name = "annotation_id")
    val annotationId: Long
)

@Dao
interface StepAnnotationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: List<StepAnnotation>)
}
