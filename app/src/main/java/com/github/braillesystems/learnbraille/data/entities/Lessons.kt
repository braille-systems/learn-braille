package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*

typealias LessonName = String
typealias LessonDesc = String

@Entity(tableName = "lessons", primaryKeys = ["id", "course_id"])
data class Lesson(
    val id: DBid,
    @ColumnInfo(name = "course_id")
    val courseId: DBid,
    val name: LessonName,
    val description: LessonDesc
)

@Dao
interface LessonDao {

    @Insert
    suspend fun insert(lessons: List<Lesson>)

    @Query(
        """
        select * from lessons
        where course_id = :courseId
        order by id
        """
    )
    suspend fun allCourseLessons(courseId: DBid): List<Lesson>

    @Query("delete from lessons")
    suspend fun clear()
}
