package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*


@Entity(tableName = "decks")
data class Deck(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String = ""
)

@Dao
interface DeckDao {

    @Insert
    suspend fun insert(decks: List<Deck>)

    @Query("select * from decks where id = :id")
    suspend fun getDeck(id: Long): Deck?
}
