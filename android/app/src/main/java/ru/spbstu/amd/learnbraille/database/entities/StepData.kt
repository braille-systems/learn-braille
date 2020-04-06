package ru.spbstu.amd.learnbraille.database.entities

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
 * Add new StepData types to the when, it is only one place not checked in compile time.
 */
fun stepDataOf(string: String): StepData = string
    .split(' ', limit = 2)
    .let { (type, data) ->
        when (type) {
            Info.name -> Info(data)
            LastInfo.name -> LastInfo(data)
            InputSymbol.name -> InputSymbol(data)
            InputDots.name -> InputDots(data)
            ShowSymbol.name -> ShowSymbol(data)
            ShowDots.name -> ShowDots(data)
            else -> error("No such step type: $type")
        }
    }

sealed class BaseInfo : StepData()

/**
 * Step displays information text for the user.
 */
class Info(
    text: String
) : BaseInfo() {

    private val text = text.stepFormat()

    override val name = Companion.name
    override val data = this.text

    companion object {
        val name = Info::class.java.name
    }
}

class LastInfo(
    text: String
) : BaseInfo() {

    private val text = text.stepFormat()

    override val name = Companion.name
    override val data = this.text

    companion object {
        val name = LastInfo::class.java.name
    }
}

sealed class BaseInput : StepData()

/**
 * Step prompts the user to enter Braille dots corresponding to the printed symbol.
 */
class InputSymbol(
    val symbol: Symbol
) : BaseInput() {

    override val name = Companion.name
    override val data = symbol.toString()

    constructor(data: String) : this(
        symbolOf(
            data
        )
    )

    companion object {
        val name = InputSymbol::class.java.name
    }
}

/**
 * Step prompts the user to enter dots with specific numbers.
 */
class InputDots(
    val dots: BrailleDots
) : BaseInput() {

    override val name = Companion.name
    override val data = dots.toString()

    constructor(data: String) : this(
        BrailleDots(
            data
        )
    )

    companion object {
        val name = InputDots::class.java.name
    }
}

sealed class BaseShow : StepData()

/**
 * Step shows symbol and it's Braille representation.
 */
class ShowSymbol(
    val symbol: Symbol
) : BaseShow() {

    override val name = Companion.name
    override val data = symbol.toString()

    constructor(data: String) : this(
        symbolOf(
            data
        )
    )

    companion object {
        val name = ShowSymbol::class.java.name
    }
}

/**
 * Step shows Braille dots with specific numbers.
 */
class ShowDots(
    val dots: BrailleDots
) : BaseShow() {

    override val name = Companion.name
    override val data = dots.toString()

    constructor(data: String) : this(
        BrailleDots(
            data
        )
    )

    companion object {
        val name = ShowDots::class.java.name
    }
}

class StepDataConverters {

    @TypeConverter
    fun to(stepData: StepData) = stepData.toString()

    @TypeConverter
    fun from(string: String) = stepDataOf(string)
}

/**
 * Use with raw strings to format text for info steps.
 */
fun String.stepFormat(): String = this
    .trimMargin()
