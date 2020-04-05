package ru.spbstu.amd.learnbraille.database.entities

import androidx.room.*
import ru.spbstu.amd.learnbraille.*

@Entity(tableName = "symbol")
data class Symbol(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    val symbol: Char,

    val language: Language = Language.NONE,

    @ColumnInfo(name = "braille_dots")
    val brailleDots: BrailleDots
) {
    companion object {
        val pattern = Regex(
            """Symbol\(id=(\d+), symbol=(.), language=(\w{2,4}), brailleDots=([E|F]{6})\)"""
        )
    }
}

fun symbolOf(data: String) = Symbol.pattern
    .matchEntire(data)
    ?.groups?.let { (_, id, symbol, language, brailleDots) ->
    Symbol(
        id = id?.value?.toLong() ?: error("No id here $data"),
        symbol = symbol?.value?.first() ?: error("No symbol here $data"),
        language = Language.valueOf(
            language?.value ?: error("No language here $data")
        ),
        brailleDots = BrailleDots(
            brailleDots?.value ?: error("No braille dots here $data")
        )
    )
} ?: error("$data does not match Symbol structure")

@Dao
interface SymbolDao {

    @Insert
    fun insertSymbols(symbols: List<Symbol>)

    @Query(
        """
        SELECT * 
        FROM symbol 
        WHERE language = :language 
        ORDER BY RANDOM() 
        LIMIT 1
        """
    )
    fun getRandomSymbol(language: Language): Symbol?

    @Query("SELECT * FROM symbol WHERE symbol = :char LIMIT 1")
    fun getSymbol(char: Char): Symbol?

    @Query(
        """
        SELECT * 
        FROM symbol 
        WHERE language = :language AND braille_dots = :brailleDots 
        LIMIT 1
        """
    )
    fun getSymbol(language: Language, brailleDots: BrailleDots): Symbol?
}


