package ru.spbstu.amd.learnbraille.database

import androidx.room.*

@Entity(tableName = "user_knows_symbol")
data class UserKnowsSymbol(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "symbol_id")
    val symbolId: Long
)

@Dao
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
