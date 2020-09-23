package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*

typealias CourseName = String
typealias CourseDesc = String

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey val id: DBid,
    val name: CourseName,
    val description: CourseDesc
)

@Dao
interface CourseDao {

    @Insert
    suspend fun insert(courses: List<Course>)

    @Query("select * from courses where id = :id")
    suspend fun course(id: DBid): Course?

    @Query("delete from courses")
    suspend fun clear()
}
