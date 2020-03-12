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

@Dao
interface SymbolDao {

    @Insert
    fun insertLetters(symbols: List<Symbol>)

    @Query("SELECT * FROM symbol WHERE language = :language ORDER BY RANDOM() LIMIT 1")
    fun getRandomEntry(language: Language): Symbol?

    @Query(
        "SELECT symbol FROM symbol " +
                "WHERE braille_dots = :brailleDots " +
                "and language = :language LIMIT 1"
    )
    fun getSymbol(brailleDots: BrailleDots, language: Language): Char?

    @Query("SELECT braille_dots FROM symbol WHERE symbol = :symbol LIMIT 1")
    fun getBrailleDots(symbol: Char): BrailleDots?
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
