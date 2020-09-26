package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*

typealias StepAnnotationName = String

@Entity(tableName = "step_annotations", indices = [Index(value = ["name"], unique = true)])
data class StepAnnotation(
    @PrimaryKey val id: DBid,
    val name: StepAnnotationName
)

@Dao
interface StepAnnotationDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(annotations: List<StepAnnotation>)

    @Query("delete from step_annotations")
    suspend fun clear()
}
