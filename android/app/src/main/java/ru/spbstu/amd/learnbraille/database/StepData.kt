package ru.spbstu.amd.learnbraille.database

import androidx.room.TypeConverter

sealed class StepData

data class Info(
    val text: String
) : StepData() {

    override fun toString() = "$name $text"

    companion object {
        val name = Info::class.java.name
    }
}

data class Show(
    val symbol: Symbol
) : StepData() {

    override fun toString() = "$name $symbol"

    companion object {
        val name = Show::class.java.name
    }
}

data class Input(
    val symbol: Symbol
) : StepData() {

    override fun toString() = "$name $symbol"

    companion object {
        val name = Input::class.java.name
    }
}

class StepDataConverters {

    @TypeConverter
    fun to(stepData: StepData) = stepData.toString()

    @TypeConverter
    fun from(string: String): StepData {
        val (type, data) = string.split(' ', limit = 2)
        return when (type) {
            Info.name -> Info(data)
            Show.name -> Show(symbolOf(data))
            Input.name -> Input(symbolOf(data))
            else -> error("No such step type: $type")
        }
    }
}
