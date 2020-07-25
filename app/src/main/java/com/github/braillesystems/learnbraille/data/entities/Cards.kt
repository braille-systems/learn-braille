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

    @Query(
        """
        select materials.* from decks
        inner join cards on decks.id = cards.deck_id
        inner join materials on materials.id = cards.material_id
        where decks.id = :deckId
        order by RANDOM() limit 1
        """
    )
    suspend fun getRandomMaterialFromDeck(deckId: Long): Material?

    @Query(
        """
        select m.* from cards as c
        inner join materials as m on m.id = c.material_id
        where c.deck_id = :deckId
        and exists (
            select * from known_materials as km
            where km.user_id = :userId and km.material_id = m.id
        )
        order by RANDOM() limit 1
        """
    )
    suspend fun getRandomKnownMaterialFromDeck(userId: Long, deckId: Long): Material?

    @Query("delete from cards")
    suspend fun clear()
}
