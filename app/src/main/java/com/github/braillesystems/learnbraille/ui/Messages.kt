package com.github.braillesystems.learnbraille.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.res.inputMarkerPrintRules
import com.github.braillesystems.learnbraille.res.inputSymbolPrintRules
import com.github.braillesystems.learnbraille.res.showMarkerPrintRules
import com.github.braillesystems.learnbraille.res.showSymbolPrintRules
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsViewMode
import com.github.braillesystems.learnbraille.utils.*
import kotlinx.android.synthetic.main.fragment_flip_dialog.view.*

fun Fragment.showCorrectToast() = toast(getString(R.string.input_correct))

fun Fragment.showIncorrectToast(hint: String = "") =
    toast("${getString(R.string.input_incorrect)} $hint")

fun Fragment.dotsMode(mode: BrailleDotsViewMode): String =
    when (mode) {
        BrailleDotsViewMode.Writing -> getString(R.string.braille_dots_mode_writing)
        BrailleDotsViewMode.Reading -> getString(R.string.braille_dots_mode_reading)
    }

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

fun Context.showPrint(data: MaterialData): String =
    when (data) {
        is Symbol -> showSymbolPrintRules.getValue(data.char)
        is MarkerSymbol -> showMarkerPrintRules.getValue(data.type)
    }

fun Fragment.showPrint(data: MaterialData): String =
    contextNotNull.showPrint(data)

fun FragmentActivity.showFlipPreferenceDialog() {
    val viewGroup: ViewGroup = findViewById(android.R.id.content)
    val dialogView: View =
        LayoutInflater.from(this).inflate(R.layout.fragment_flip_dialog, viewGroup, false)
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setView(dialogView)
    builder.setTitle(getString(R.string.fragment_flip_dialog_title))
    val alertDialog: AlertDialog = builder.create()
    alertDialog.show()
    dialogView.btnOK.setOnClickListener {
        val flipPreferenceOn = dialogView.radioButtonFlip.isChecked
        // TODO save preference & never ask again
        alertDialog.hide()
    }
}
