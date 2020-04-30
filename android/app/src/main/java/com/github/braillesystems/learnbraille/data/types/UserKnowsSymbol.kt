package com.github.braillesystems.learnbraille.data.types

import androidx.room.*

@Entity(tableName = "user_knows_symbol", primaryKeys = ["user_id", "symbol_id"])
data class UserKnowsSymbol(

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "symbol_id")
    val symbolId: Long
)

@Dao
interface UserKnowsSymbolDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertKnowledge(knowledge: UserKnowsSymbol)

    @Query(
        """
        SELECT symbol.*
        FROM user_knows_symbol AS uks
        INNER JOIN symbol on symbol.id = uks.symbol_id
        WHERE user_id = :userId
        ORDER BY RANDOM()
        LIMIT 1
        """
    )
    suspend fun getRandomKnownSymbol(userId: Long): Symbol?
}
