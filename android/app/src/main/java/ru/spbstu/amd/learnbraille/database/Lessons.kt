package ru.spbstu.amd.learnbraille.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey

@Entity(tableName = "lesson")
data class Lesson(

    @PrimaryKey(autoGenerate = false)
    val id: Long,

    val name: String
)

@Dao
interface LessonDao {

    @Insert
    fun insertLessons(lessons: List<Lesson>)
}

// TODO load from resources
val PREPOPULATE_LESSONS = listOf(
    Lesson(id = 1, name = "Вводное введение")
)
