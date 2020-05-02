package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*


@Entity(tableName = "cards")
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

    // TODO
}
