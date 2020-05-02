package com.github.braillesystems.learnbraille.data.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey


@Entity(tableName = "materials")
data class Material(
    @PrimaryKey
    val id: Long,
    val data: MaterialData
)

@Dao
interface MaterialDao {

    @Insert
    suspend fun insert(materials: List<Material>)

    // TODO MaterialDao
}


// TODO
// companion object {
//        val pattern = Regex(
//            """Symbol\(id=(\d+), symbol=(.), language=(\w{2,4}), brailleDots=([E|F]{6})\)"""
//        )
//    }
//}
//
//fun symbolOf(data: String) = Symbol.pattern.matchEntire(data)?.groups
//    ?.let { (_, id, symbol, language, brailleDots) ->
//        com.github.braillesystems.learnbraille.data.entities.experimental.Symbol(
//            id = id?.value?.toLong() ?: error("No id here $data"),
//            symbol = symbol?.value?.first() ?: error("No symbol here $data"),
//            language = Language.valueOf(
//                language?.value ?: error("No language here $data")
//            ),
//            brailleDots = BrailleDots(
//                brailleDots?.value ?: error("No braille dots here $data")
//            )
//        )
//    } ?: error("$data does not match Symbol structure")
//
//@Dao
//interface SymbolDao {
//
//    @Insert
//    suspend fun insertSymbols(symbols: List<com.github.braillesystems.learnbraille.data.entities.experimental.Symbol>)
//
//    @Query(
//        """
//        SELECT *
//        FROM symbol
//        WHERE language = :language
//        ORDER BY RANDOM()
//        LIMIT 1
//        """
//    )
//    suspend fun getRandomSymbol(language: Language): com.github.braillesystems.learnbraille.data.entities.experimental.Symbol?
//
//    @Query("SELECT * FROM symbol WHERE symbol = :char LIMIT 1")
//    suspend fun getSymbol(char: Char): com.github.braillesystems.learnbraille.data.entities.experimental.Symbol?
//
//    @Query(
//        """
//        SELECT *
//        FROM symbol
//        WHERE language = :language AND braille_dots = :brailleDots
//        LIMIT 1
//        """
//    )
//    suspend fun getSymbol(language: Language, brailleDots: BrailleDots): com.github.braillesystems.learnbraille.data.entities.experimental.Symbol?
//
//    @Query("DELETE FROM symbol")
//    suspend fun deleteAll()
//}
