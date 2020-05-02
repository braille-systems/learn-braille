package com.github.braillesystems.learnbraille.data.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey


@Entity(tableName = "annotations")
data class Annotation(
    @PrimaryKey
    val id: Long,
    val name: String // TODO mark unique
)

@Dao
interface AnnotationsDao {

    @Insert
    suspend fun insert(annotations: List<Annotation>)

    // TODO AnnotationsDao
}
