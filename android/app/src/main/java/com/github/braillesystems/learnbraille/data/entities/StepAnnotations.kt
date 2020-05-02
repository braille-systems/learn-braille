package com.github.braillesystems.learnbraille.data.entities

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert


@Entity(tableName = "step_annotations", primaryKeys = ["step_id", "annotation_id"])
data class StepAnnotation(
    @ColumnInfo(name = "step_id")
    val stepId: Long,
    @ColumnInfo(name = "annotation_id")
    val annotationId: Long
)

@Dao
interface StepAnnotationDao {

    @Insert
    suspend fun insert(data: List<StepAnnotation>)

    // TODO StepAnnotationsDao
}
