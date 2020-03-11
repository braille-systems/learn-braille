package ru.spbstu.amd.learnbraille.database

import androidx.room.TypeConverter
import ru.spbstu.amd.learnbraille.database.BrailleDot.E
import ru.spbstu.amd.learnbraille.database.BrailleDot.F

enum class BrailleDot {
    E,  // Empty
    F;  // Filled
}

fun brailleDot(b: Boolean) = if (b) F else E

data class BrailleDots(private val dots: String) {

    constructor(
        b1: BrailleDot = E, b2: BrailleDot = E, b3: BrailleDot = E,
        b4: BrailleDot = E, b5: BrailleDot = E, b6: BrailleDot = E
    ) : this(
        listOf(b1, b2, b3, b4, b5, b6)
    )

    constructor(dots: BooleanArray) : this(
        dots.map(::brailleDot)
    )

    constructor(dots: List<BrailleDot>) : this(
        dots.joinToString(transform = BrailleDot::toString)
    ) {
        require(dots.size == 6) {
            "Only 6 dots braille notation supported"
        }
    }

    override fun toString() = dots
}

class BrailleDotsConverters {

    @TypeConverter
    fun to(brailleDots: BrailleDots) = brailleDots.toString()

    @TypeConverter
    fun from(data: String): BrailleDots = BrailleDots(data)
}
