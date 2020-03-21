package ru.spbstu.amd.learnbraille.database

import androidx.room.*

@Entity(tableName = "symbol")
data class Symbol(

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    val symbol: Char,

    val language: Language,

    @ColumnInfo(name = "braille_dots")
    val brailleDots: BrailleDots
)

private operator fun MatchGroupCollection.component1() = get(0)
private operator fun MatchGroupCollection.component2() = get(1)
private operator fun MatchGroupCollection.component3() = get(2)
private operator fun MatchGroupCollection.component4() = get(3)
private operator fun MatchGroupCollection.component5() = get(4)

fun symbolOf(data: String): Symbol {
    val pattern = """Symbol\(id=(\d+), symbol=(\w), language=(\w+), brailleDots=([E|F]{6})\)"""
    val (_, id, symbol, language, brailleDots) = Regex(pattern).matchEntire(data)?.groups
        ?: throw IllegalArgumentException("$data does not match Symbol structure")

    return Symbol(
        id = id?.value?.toLong()
            ?: throw IllegalArgumentException("No id here $data"),

        symbol = symbol?.value?.first()
            ?: throw IllegalArgumentException("No symbol here $data"),

        language = Language.valueOf(
            language?.value
                ?: throw IllegalArgumentException("No language here $data")
        ),

        brailleDots = BrailleDots(
            brailleDots?.value
                ?: throw IllegalArgumentException("No braille dots here $data")
        )
    )
}

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
    fun getRandomEntry(language: Language): Symbol?

    @Query(
        """
            SELECT symbol 
            FROM symbol 
            WHERE braille_dots = :brailleDots AND language = :language 
            LIMIT 1
            """
    )
    fun getSymbol(brailleDots: BrailleDots, language: Language): Char?

    @Query("SELECT braille_dots FROM symbol WHERE symbol = :symbol LIMIT 1")
    fun getBrailleDots(symbol: Char): BrailleDots?
}


