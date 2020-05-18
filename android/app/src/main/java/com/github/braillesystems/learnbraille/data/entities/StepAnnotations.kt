package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*


@Entity(tableName = "step_annotations", indices = [Index(value = ["name"], unique = true)])
data class StepAnnotation(
    @PrimaryKey val id: Long,
    val name: String
)

@Dao
interface StepAnnotationDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(annotations: List<StepAnnotation>)
}
