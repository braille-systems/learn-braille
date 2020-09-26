package com.github.braillesystems.learnbraille.data.entities

import androidx.room.*

typealias DeckTag = String

@Entity(tableName = "decks")
data class Deck(
    @PrimaryKey val id: DBid,
    val tag: DeckTag
)

@Dao
interface DeckDao {

    @Insert
    suspend fun insert(decks: List<Deck>)

    @Query("select * from decks where id = :id")
    suspend fun deck(id: DBid): Deck?

    @Query("select * from decks")
    suspend fun allDecks(): List<Deck>

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
    suspend fun availableDecks(userId: DBid): List<Deck>

    @Query("delete from decks")
    suspend fun clear()
}
