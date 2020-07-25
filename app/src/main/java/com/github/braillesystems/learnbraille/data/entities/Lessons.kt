package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*


@Entity(tableName = "lessons", primaryKeys = ["id", "course_id"])
data class Lesson(
    val id: Long,
    @ColumnInfo(name = "course_id")
    val courseId: Long,
    val name: String,
    val description: String
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
    suspend fun getAllCourseLessons(courseId: Long): List<Lesson>

    @Query("delete from lessons")
    suspend fun clear()
}
