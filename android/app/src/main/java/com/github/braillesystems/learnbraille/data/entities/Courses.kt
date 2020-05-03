package com.github.braillesystems.learnbraille.data.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey


@Entity(tableName = "courses")
data class Course(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String
)

@Dao
interface CourseDao {

    @Insert
    suspend fun insert(courses: List<Course>)
}
