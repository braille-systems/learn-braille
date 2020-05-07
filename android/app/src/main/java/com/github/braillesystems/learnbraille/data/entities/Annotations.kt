package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*


@Entity(tableName = "annotations", indices = [Index(value = ["name"], unique = true)])
data class Annotation(
    @PrimaryKey val id: Long,
    val name: String
)

@Dao
interface AnnotationsDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(annotations: List<Annotation>)
}
