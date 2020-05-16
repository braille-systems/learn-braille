package com.github.braillesystems.learnbraille.data.entities

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert


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
}
