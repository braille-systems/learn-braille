package ru.spbstu.amd.learnbraille.database

import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey

@Entity(tableName = "step_type")
data class StepType(

    @PrimaryKey
    val id: Long = 0,

    val name: String
)

interface StepTypeDao {

    @Insert
    fun insertTypes(types: List<StepType>)
}
