package ru.spbstu.amd.learnbraille.database

import androidx.room.TypeConverter

sealed class StepData {

    protected abstract val name: String
    protected abstract val data: String

    override fun toString() = "$name $data"
}

class Info(
    val text: String
) : StepData() {

    override val name = Companion.name
    override val data = text

    companion object {
        val name = Info::class.java.name
    }
}

class InputSymbol(
    val symbol: Symbol
) : StepData() {

    override val name = Companion.name
    override val data = symbol.toString()

    companion object {
        val name = InputSymbol::class.java.name
    }
}

class InputDots(
    val dots: BrailleDots
) : StepData() {

    override val name = Companion.name
    override val data = dots.toString()

    companion object {
        val name = InputDots::class.java.name
    }
}

class ShowSymbol(
    val symbol: Symbol
) : StepData() {

    override val name = Companion.name
    override val data = symbol.toString()

    companion object {
        val name = ShowSymbol::class.java.name
    }
}

class ShowDots(
    val dots: BrailleDots
) : StepData() {

    override val name = Companion.name
    override val data = dots.toString()

    companion object {
        val name = ShowDots::class.java.name
    }
}

fun stepDataOf(string: String): StepData = string
    .split(' ', limit = 2)
    .let { (type, data) ->
        when (type) {
            Info.name -> Info(data)
            InputSymbol.name -> InputSymbol(symbolOf(data))
            InputDots.name -> InputDots(BrailleDots(data))
            ShowSymbol.name -> ShowSymbol(symbolOf(data))
            ShowDots.name -> ShowDots(BrailleDots(data))
            else -> error("No such step type: $type")
        }
    }

class StepDataConverters {

    @TypeConverter
    fun to(stepData: StepData) = stepData.toString()

    @TypeConverter
    fun from(string: String): StepData = stepDataOf(string)
}
