package com.github.braillesystems.learnbraille.data.types

import androidx.room.*

@Entity(tableName = "lesson")
data class Lesson(

    @PrimaryKey(autoGenerate = false)
    val id: Long,

    val name: String
)

@Dao
interface LessonDao {

    @Insert
    suspend fun insertLessons(lessons: List<Lesson>)

    @Query("DELETE FROM lesson")
    suspend fun deleteAll()
}
