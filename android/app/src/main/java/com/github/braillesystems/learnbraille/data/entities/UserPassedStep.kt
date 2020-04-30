package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*

@Entity(tableName = "user_passed_step", primaryKeys = ["user_id", "step_id"])
data class UserPassedStep(

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "step_id")
    val stepId: Long
)

@Dao
interface UserPassedStepDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPassedStep(passedStep: UserPassedStep)
}
