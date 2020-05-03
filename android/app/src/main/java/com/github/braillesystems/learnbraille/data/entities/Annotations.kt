package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*


@Entity(tableName = "annotations")
data class Annotation(
    @PrimaryKey
    val id: Long,
    val name: String // TODO mark unique
)

@Dao
interface AnnotationsDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(annotations: List<Annotation>)
}
