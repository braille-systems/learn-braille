package com.github.braillesystems.learnbraille.ui

import android.content.Context
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.res.inputMarkerPrintRules
import com.github.braillesystems.learnbraille.res.inputSymbolPrintRules
import com.github.braillesystems.learnbraille.utils.*

fun Fragment.showCorrectToast() = toast(getString(R.string.input_correct))

fun Fragment.showIncorrectToast(hint: String = "") =
    toast("${getString(R.string.input_incorrect)} $hint")

private val Context.dotsHintRules by lazyWithContext<Context, List<String>> {
    listOf(
        getString(R.string.input_dots_hint_1),
        getString(R.string.input_dots_hint_2),
        getString(R.string.input_dots_hint_3),
        getString(R.string.input_dots_hint_4),
        getString(R.string.input_dots_hint_5),
        getString(R.string.input_dots_hint_6)
    )
}

fun Fragment.showHintDotsToast(expectedDots: BrailleDots) {
    val template = getString(R.string.input_dots_hint_template)
    val hint = expectedDots
        .filled
        .joinToString(separator = ", ") {
            contextNotNull.dotsHintRules[it - 1]
        }
    checkedToast(template.format(hint))
}

fun Fragment.showHintToast(expectedDots: BrailleDots) =
    checkedToast(getString(R.string.input_hint_template).format(expectedDots.spelling))

fun Context.inputPrint(data: MaterialData): String =
    when (data) {
        is Symbol -> inputSymbolPrintRules.getValue(data.char)
        is MarkerSymbol -> inputMarkerPrintRules.getValue(data.type)
    }

fun Fragment.inputPrint(data: MaterialData): String =
    contextNotNull.inputPrint(data)
