package ru.spbstu.amd.learnbraille.database

import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey

@Entity(tableName = "lesson")
data class Lesson(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    val name: String
)

interface LessonDao {

    @Insert
    fun insertLessons(lessons: List<Lesson>)
}
