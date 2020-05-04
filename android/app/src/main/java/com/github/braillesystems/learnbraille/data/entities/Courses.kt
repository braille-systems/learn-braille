package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*


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

    @Query("select * from courses where id = :id")
    suspend fun getCourse(id: Long): Course?
}
