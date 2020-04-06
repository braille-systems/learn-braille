package ru.spbstu.amd.learnbraille.database.entities

import androidx.room.TypeConverter
import ru.spbstu.amd.learnbraille.database.entities.BrailleDot.E

enum class BrailleDot {
    E,  // Empty
    F;  // Filled

    companion object {
        fun valueOf(b: Boolean) = if (b) F else E
        fun valueOf(c: Char) = valueOf(c.toString())
    }
}

data class BrailleDots(
    val b1: BrailleDot = E, val b2: BrailleDot = E, val b3: BrailleDot = E,
    val b4: BrailleDot = E, val b5: BrailleDot = E, val b6: BrailleDot = E
) {

    constructor(dots: BooleanArray) : this(
        dots.map {
            BrailleDot.valueOf(
                it
            )
        }
    )

    constructor(dots: List<BrailleDot>) : this(
        b1 = dots[0],
        b2 = dots[1],
        b3 = dots[2],
        b4 = dots[3],
        b5 = dots[4],
        b6 = dots[5]
    ) {
        require(dots.size == 6) {
            "Only 6 dots braille notation supported"
        }
    }

    constructor(string: String) : this(
        string.toCharArray().map {
            BrailleDot.valueOf(
                it
            )
        }
    )

    override fun toString() = "$b1$b2$b3$b4$b5$b6"
}

val BrailleDots.list: List<BrailleDot>
    get() = listOf(b1, b2, b3, b4, b5, b6)

val BrailleDots.spelling: String
    get() = list
        .mapIndexed { index, brailleDot ->
            if (brailleDot == BrailleDot.F) {
                (index + 1).toString()
            } else {
                null
            }
        }
        .filterNotNull()
        .joinToString(separator = " ")

class BrailleDotsConverters {

    @TypeConverter
    fun to(brailleDots: BrailleDots) = brailleDots.toString()

    @TypeConverter
    fun from(data: String): BrailleDots =
        BrailleDots(data)
}
