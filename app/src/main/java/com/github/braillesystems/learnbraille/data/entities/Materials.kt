package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*
import kotlinx.serialization.Serializable

@Entity(tableName = "materials")
@Serializable
data class Material(
    @PrimaryKey val id: DBid,
    val data: MaterialData
)

@Dao
interface MaterialDao {

    @Insert
    suspend fun insert(materials: List<Material>)

    @Query("select * from materials where id = :id")
    suspend fun getMaterial(id: DBid): Material?

    @Query("select * from materials order by RANDOM() limit 1")
    suspend fun getRandomMaterial(): Material?

    @Query("Delete from materials")
    suspend fun clear()
}
