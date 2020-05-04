package com.github.braillesystems.learnbraille.data.entities

import androidx.room.TypeConverter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json


/**
 * There are specific step types in the app.
 * They differs by visual representation and by the data they contain.
 *
 * Step type can be determined by `is` check.
 */
@Serializable
sealed class StepData

class StepDataConverters {

    @UnstableDefault
    @TypeConverter
    fun to(stepData: StepData) = Json.stringify(StepData.serializer(), stepData)

    @UnstableDefault
    @TypeConverter
    fun from(string: String) = Json.parse(StepData.serializer(), string)
}


/**
 * Represents step types with information.
 */
@Serializable
sealed class BaseInfo : StepData()

/**
 * Step displays information text for the user.
 */
@Serializable
data class Info(
    @SerialName("info")
    val text: String
) : BaseInfo()

/**
 * Info step without `prev` button.
 */
@Serializable
data class FirstInfo(
    @SerialName("info")
    val text: String
) : BaseInfo()

/**
 * Info step without `next` button.
 */
@Serializable
data class LastInfo(
    @SerialName("info")
    val text: String
) : BaseInfo()


@Serializable
sealed class BaseInput : StepData()

/**
 * Step prompts the user to enter something.
 */
@Serializable
data class Input(
    val material: Material
) : BaseInput()

/**
 * Step prompts the user to enter dots with specific numbers.
 *
 * @param text Special text of default braille dots spelling.
 * Generated one will be displayed if `null`.
 */
@Serializable
data class InputDots(
    val text: String?,
    val dots: BrailleDots
) : BaseInput()


@Serializable
sealed class BaseShow : StepData()

/**
 * Step shows something.
 */
@Serializable
data class Show(
    val material: Material
) : BaseShow()

/**
 * Step shows Braille dots with specific numbers.
 *
 * @param text Special text of default braille dots spelling.
 * Generated one will be displayed if `null`.
 */
@Serializable
data class ShowDots(
    val text: String?,
    val dots: BrailleDots
) : BaseShow()


/**
 * Use with raw strings to format text for info steps.
 */
// TODO stepFormat
fun String.stepFormat(): String = this
    .trimMargin()
