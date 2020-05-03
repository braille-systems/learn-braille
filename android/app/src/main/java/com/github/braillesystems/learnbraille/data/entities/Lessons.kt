package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*


@Entity(tableName = "lesson")
data class Lesson(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String,
    @ColumnInfo(name = "course_id")
    val courseId: Long
)

@Dao
interface LessonDao {

    @Insert
    suspend fun insert(lessons: List<Lesson>)
}
