package ru.spbstu.amd.learnbraille.database

import androidx.room.*

@Entity(tableName = "user_passed_step")
data class UserPassedStep(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "step_id")
    val stepId: Long
)

@Dao
interface UserPassedStepDao {

    @Insert
    fun insertPassedStep(passedStep: UserPassedStep)
}
