package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*


@Entity(tableName = "known_materials", primaryKeys = ["user_id", "material_id"])
data class KnownMaterial(
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "material_id")
    val materialId: Long
)

@Dao
interface KnownMaterialDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(knowledge: KnownMaterial)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(knowledge: List<KnownMaterial>)

    @Query("delete from known_materials")
    suspend fun clear()
}
