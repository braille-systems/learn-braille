package com.github.braillesystems.learnbraille.data.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey


@Entity(tableName = "materials")
data class Material(
    @PrimaryKey val id: Long,
    val data: MaterialData
)

@Dao
interface MaterialDao {

    @Insert
    suspend fun insert(materials: List<Material>)
}
