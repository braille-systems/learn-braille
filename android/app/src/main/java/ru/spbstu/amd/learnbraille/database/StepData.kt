package ru.spbstu.amd.learnbraille.database

import androidx.room.TypeConverter

/**
 * There are specific step types in the app.
 * They differs by visual representation and by the data they contain.
 *
 * Step type can be determined by `is` check.
 */
sealed class StepData {

    protected abstract val name: String
    protected abstract val data: String

    override fun toString() = "$name $data"
}

/**
 * Step displays information text for the user.
 */
class Info(
    text: String
) : StepData() {

    private val text = text.stepFormat()

    override val name = Companion.name
    override val data = this.text

    companion object {
        val name = Info::class.java.name
    }
}

/**
 * Step prompts the user to enter Braille dots corresponding to the printed symbol.
 */
class InputSymbol(
    val symbol: Symbol
) : StepData() {

    override val name = Companion.name
    override val data = symbol.toString()

    constructor(data: String) : this(symbolOf(data))

    companion object {
        val name = InputSymbol::class.java.name
    }
}

/**
 * Step prompts the user to enter dots with specific numbers.
 */
class InputDots(
    val dots: BrailleDots
) : StepData() {

    override val name = Companion.name
    override val data = dots.toString()

    constructor(data: String) : this(BrailleDots(data))

    companion object {
        val name = InputDots::class.java.name
    }
}

/**
 * Step shows symbol and it's Braille representation.
 */
class ShowSymbol(
    val symbol: Symbol
) : StepData() {

    override val name = Companion.name
    override val data = symbol.toString()

    constructor(data: String) : this(symbolOf(data))

    companion object {
        val name = ShowSymbol::class.java.name
    }
}

/**
 * Step shows Braille dots with specific numbers.
 */
class ShowDots(
    val dots: BrailleDots
) : StepData() {

    override val name = Companion.name
    override val data = dots.toString()

    constructor(data: String) : this(BrailleDots(data))

    companion object {
        val name = ShowDots::class.java.name
    }
}

fun stepDataOf(string: String): StepData = string
    .split(' ', limit = 2)
    .let { (type, data) ->
        when (type) {
            Info.name -> Info(data)
            InputSymbol.name -> InputSymbol(data)
            InputDots.name -> InputDots(data)
            ShowSymbol.name -> ShowSymbol(data)
            ShowDots.name -> ShowDots(data)
            else -> error("No such step type: $type")
        }
    }

class StepDataConverters {

    @TypeConverter
    fun to(stepData: StepData) = stepData.toString()

    @TypeConverter
    fun from(string: String): StepData = stepDataOf(string)
}

/**
 * Use with raw strings to format text for info steps.
 */
fun String.stepFormat(): String = this
    .trimMargin()
