package com.github.braillesystems.learnbraille.data.entities

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity


@Entity(tableName = "known_materials", primaryKeys = ["user_id", "material_id"])
data class KnownMaterials(
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "material_id")
    val materialId: Long
)

@Dao
interface KnownMaterialsDao {
    // TODO KnownMaterialsDao
}
