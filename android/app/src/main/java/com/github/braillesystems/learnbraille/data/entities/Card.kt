package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*


@Entity(tableName = "cards", primaryKeys = ["deck_id", "material_id"])
data class Card(
    @ColumnInfo(name = "deck_id")
    val deckId: Long,
    @ColumnInfo(name = "material_id")
    val materialId: Long
)

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(cards: List<Card>)

    @Query("select * from materials order by RANDOM() limit 1")
    suspend fun getNextMaterial(): Material?
}
