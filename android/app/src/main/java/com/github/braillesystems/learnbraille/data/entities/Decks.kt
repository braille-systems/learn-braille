package com.github.braillesystems.learnbraille.data.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey


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
}
