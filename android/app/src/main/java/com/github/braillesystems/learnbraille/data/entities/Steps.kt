package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*


@Entity(tableName = "steps")
data class Step(
    @PrimaryKey val id: Long,
    val data: StepData,
    @ColumnInfo(name = "lesson_id")
    val lessonId: Long
)

@Dao
interface StepDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(steps: List<Step>)

    @Query("select * from steps where id = :id")
    suspend fun getStep(id: Long): Step?
}
