package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*

@Entity(tableName = "user_last_step", primaryKeys = ["user_id"])
data class UserLastStep(

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "step_id")
    val stepId: Long
)

@Dao
interface UserLastStepDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLastStep(currentStep: UserLastStep)
}
