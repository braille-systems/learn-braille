package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*


@Entity(tableName = "step")
data class Step(
    @PrimaryKey val id: Long,
    val data: StepData,
    @ColumnInfo(name = "lesson_id")
    val lessonId: Long
)

@Dao
interface StepDao {

    @Insert
    suspend fun insert(steps: List<Step>)
}
