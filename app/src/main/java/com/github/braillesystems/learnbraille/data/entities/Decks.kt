package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*


@Entity(tableName = "decks")
data class Deck(
    @PrimaryKey val id: Long,
    val tag: String
)

@Dao
interface DeckDao {

    @Insert
    suspend fun insert(decks: List<Deck>)

    @Query("select * from decks where id = :id")
    suspend fun getDeck(id: Long): Deck?

    @Query("select * from decks")
    suspend fun getAllDecks(): List<Deck>

    @Query(
        """
        select * from decks as d
        where exists (
            select * from cards as c
            inner join known_materials as km on km.material_id = c.material_id
            where km.user_id = :userId and c.deck_id = d.id
        )
        order by d.id
        """
    )
    suspend fun getAvailableDecks(userId: Long): List<Deck>

    @Query("delete from decks")
    suspend fun clear()
}
