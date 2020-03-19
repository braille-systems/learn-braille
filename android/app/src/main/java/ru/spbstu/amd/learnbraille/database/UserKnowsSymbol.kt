package ru.spbstu.amd.learnbraille.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query

@Entity(tableName = "user_knows_symbol")
data class UserKnowsSymbol(

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "symbol_id")
    val symbolId: Long
)

interface UserKnowsSymbolDao {

    @Insert
    fun insertKnowledge(knowledge: UserKnowsSymbol)

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
    fun getRandomKnownSymbol(userId: Long): Symbol?
}
