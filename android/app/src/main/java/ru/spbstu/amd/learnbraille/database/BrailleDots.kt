package ru.spbstu.amd.learnbraille.database

import androidx.room.TypeConverter
import ru.spbstu.amd.learnbraille.database.BrailleDot.E
import ru.spbstu.amd.learnbraille.database.BrailleDot.F

enum class BrailleDot {
    E,  // Empty
    F;  // Filled
}

fun brailleDotOf(b: Boolean) = if (b) F else E

data class BrailleDots(
    val b1: BrailleDot = E, val b2: BrailleDot = E, val b3: BrailleDot = E,
    val b4: BrailleDot = E, val b5: BrailleDot = E, val b6: BrailleDot = E
) {

    constructor(dots: BooleanArray) : this(
        dots.map(::brailleDotOf)
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

    override fun toString() = "$b1$b2$b3$b4$b5$b6"
}

/**
 * Convert braille dots string representation [E|F]{6} to BrailleDots object.
 */
fun brailleDotsOf(string: String) = BrailleDots(
    string.split("").map(BrailleDot::valueOf)
)

class BrailleDotsConverters {

    @TypeConverter
    fun to(brailleDots: BrailleDots) = brailleDots.toString()

    @TypeConverter
    fun from(data: String): BrailleDots = brailleDotsOf(data)
}
