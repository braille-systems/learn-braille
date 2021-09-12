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
 * All texts are in html format. Use .parseAsHtml android extension function.
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

typealias HtmlText = String

/**
 * Represents step types with information.
 */
@Serializable
sealed class BaseInfo : StepData() {
    abstract val text: HtmlText
}

/**
 * Step displays information text for the user.
 */
@Serializable
data class Info(
    @SerialName("info")
    override val text: HtmlText
) : BaseInfo()

/**
 * Info step without `prev` button.
 */
@Serializable
data class FirstInfo(
    @SerialName("info")
    override val text: HtmlText
) : BaseInfo()

/**
 * Info step without `next` button.
 */
@Serializable
data class LastInfo(
    @SerialName("info")
    override val text: HtmlText
) : BaseInfo()


@Serializable
sealed class BaseInput : StepData() {
    abstract val brailleDots: BrailleDots
}

/**
 * Step prompts the user to enter something.
 */
@Serializable
data class Input(
    val material: Material
) : BaseInput() {

    override val brailleDots: BrailleDots
        get() = when (material.data) {
            is OneBrailleSymbol -> material.data.brailleDots
        }
}

typealias Phrase = List<Material>

@Serializable
data class InputPhraseLetter(
    val phrase: Phrase,
    val pos: Int
) : BaseInput() {

    override val brailleDots: BrailleDots
        get() = when (phrase[pos].data) {
            is OneBrailleSymbol -> material.data.brailleDots
        }
}

/**
 * Step prompts the user to enter dots with specific numbers.
 *
 * @param text Special text of default braille dots spelling.
 * Generated one will be displayed if `null`.
 */
@Serializable
data class InputDots(
    val text: HtmlText?,
    @SerialName("dots") // backward compatibility
    override val brailleDots: BrailleDots
) : BaseInput()


@Serializable
sealed class BaseShow : StepData() {
    abstract val brailleDots: BrailleDots
}

/**
 * Step shows something.
 */
@Serializable
data class Show(
    val material: Material
) : BaseShow() {

    override val brailleDots: BrailleDots
        get() = when (material.data) {
            is OneBrailleSymbol -> material.data.brailleDots
        }
}

/**
 * Step shows Braille dots with specific numbers.
 *
 * @param text Special text of default braille dots spelling.
 * Generated one will be displayed if `null`.
 */
@Serializable
data class ShowDots(
    val text: HtmlText?,
    @SerialName("dots")
    override val brailleDots: BrailleDots
) : BaseShow()
