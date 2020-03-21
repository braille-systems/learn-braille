package ru.spbstu.amd.learnbraille.database

import androidx.room.*
import ru.spbstu.amd.learnbraille.database.BrailleDot.E
import ru.spbstu.amd.learnbraille.database.BrailleDot.F
import ru.spbstu.amd.learnbraille.database.Language.RU

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
        ?: error("$data does not match Symbol structure")

    return Symbol(
        id = id?.value?.toLong() ?: error("No id here $data"),
        symbol = symbol?.value?.first() ?: error("No symbol here $data"),
        language = Language.valueOf(
            language?.value ?: error("No language here $data")
        ),
        brailleDots = BrailleDots(
            brailleDots?.value ?: error("No braille dots here $data")
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

internal val PREPOPULATE_LETTERS = listOf(
    Symbol(symbol = 'А', language = RU, brailleDots = BrailleDots(F, E, E, E, E, E)),
    Symbol(symbol = 'Б', language = RU, brailleDots = BrailleDots(F, F, E, E, E, E)),
    Symbol(symbol = 'В', language = RU, brailleDots = BrailleDots(E, F, E, F, F, F)),
    Symbol(symbol = 'Г', language = RU, brailleDots = BrailleDots(F, F, E, F, F, E)),
    Symbol(symbol = 'Д', language = RU, brailleDots = BrailleDots(F, E, E, F, F, E)),
    Symbol(symbol = 'Е', language = RU, brailleDots = BrailleDots(F, E, E, E, F, E)),
    Symbol(symbol = 'Ё', language = RU, brailleDots = BrailleDots(F, E, E, E, E, F)),
    Symbol(symbol = 'Ж', language = RU, brailleDots = BrailleDots(E, F, E, F, F, E)),
    Symbol(symbol = 'З', language = RU, brailleDots = BrailleDots(F, E, F, E, F, F)),
    Symbol(symbol = 'И', language = RU, brailleDots = BrailleDots(E, F, E, F, E, E)),
    Symbol(symbol = 'Й', language = RU, brailleDots = BrailleDots(F, F, F, F, E, F)),
    Symbol(symbol = 'К', language = RU, brailleDots = BrailleDots(F, E, F, E, E, E)),
    Symbol(symbol = 'Л', language = RU, brailleDots = BrailleDots(F, F, F, E, E, E)),
    Symbol(symbol = 'М', language = RU, brailleDots = BrailleDots(F, E, F, F, E, E)),
    Symbol(symbol = 'Н', language = RU, brailleDots = BrailleDots(F, E, F, F, F, E)),
    Symbol(symbol = 'О', language = RU, brailleDots = BrailleDots(F, E, F, E, F, E)),
    Symbol(symbol = 'П', language = RU, brailleDots = BrailleDots(F, F, F, F, E, E)),
    Symbol(symbol = 'Р', language = RU, brailleDots = BrailleDots(F, F, F, E, F, E)),
    Symbol(symbol = 'С', language = RU, brailleDots = BrailleDots(E, F, F, F, E, E)),
    Symbol(symbol = 'Т', language = RU, brailleDots = BrailleDots(E, F, F, F, F, E)),
    Symbol(symbol = 'У', language = RU, brailleDots = BrailleDots(F, E, F, E, E, F)),
    Symbol(symbol = 'Ф', language = RU, brailleDots = BrailleDots(F, F, E, F, E, E)),
    Symbol(symbol = 'Х', language = RU, brailleDots = BrailleDots(F, F, E, E, F, E)),
    Symbol(symbol = 'Ц', language = RU, brailleDots = BrailleDots(F, E, E, F, E, E)),
    Symbol(symbol = 'Ч', language = RU, brailleDots = BrailleDots(F, F, F, F, F, E)),
    Symbol(symbol = 'Ш', language = RU, brailleDots = BrailleDots(F, E, E, E, F, F)),
    Symbol(symbol = 'Щ', language = RU, brailleDots = BrailleDots(F, E, F, F, E, F)),
    Symbol(symbol = 'Ъ', language = RU, brailleDots = BrailleDots(F, F, F, E, F, F)),
    Symbol(symbol = 'Ы', language = RU, brailleDots = BrailleDots(E, F, F, F, E, F)),
    Symbol(symbol = 'Ь', language = RU, brailleDots = BrailleDots(E, F, F, F, F, F)),
    Symbol(symbol = 'Э', language = RU, brailleDots = BrailleDots(E, F, E, F, E, F)),
    Symbol(symbol = 'Ю', language = RU, brailleDots = BrailleDots(F, F, E, E, F, F)),
    Symbol(symbol = 'Я', language = RU, brailleDots = BrailleDots(F, F, E, F, E, F))
)
