package ru.spbstu.amd.learnbraille.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Insert

@Entity(tableName = "user_passed_step")
data class UserPassedStep(

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "step_id")
    val stepId: Long
)

interface UserPassedStepDao {

    @Insert
    fun insertPassedStep(passedStep: UserPassedStep)
}
