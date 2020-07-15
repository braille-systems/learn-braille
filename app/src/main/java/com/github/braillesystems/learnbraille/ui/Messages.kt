package com.github.braillesystems.learnbraille.ui

import android.content.Context
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.BrailleDot
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.entities.list
import com.github.braillesystems.learnbraille.data.entities.spelling
import com.github.braillesystems.learnbraille.res.inputSymbolPrintRules
import com.github.braillesystems.learnbraille.res.showSymbolPrintRules
import com.github.braillesystems.learnbraille.utils.*
import timber.log.Timber

enum class PrintMode {
    INPUT, SHOW
}

fun Fragment.showCorrectToast(): Unit = toast(getString(R.string.input_correct))

fun Fragment.showIncorrectToast(c: Char? = null): Unit =
    if (c == null) toast(getString(R.string.input_incorrect))
    else toast(
        "${getString(R.string.input_incorrect)} " +
                printString(c, PrintMode.INPUT).orEmpty()
    )

val Context.dotsHintRules by lazyWithContext<Context, List<String>> {
    listOf(
        getString(R.string.input_dots_hint_1),
        getString(R.string.input_dots_hint_2),
        getString(R.string.input_dots_hint_3),
        getString(R.string.input_dots_hint_4),
        getString(R.string.input_dots_hint_5),
        getString(R.string.input_dots_hint_6)
    )
}

fun Fragment.showHintDotsToast(expectedDots: BrailleDots) =
    getString(R.string.input_dots_hint_template)
        .format(
            expectedDots.list
                .mapIndexedNotNull { index, brailleDot ->
                    if (brailleDot == BrailleDot.F) application.dotsHintRules[index] else null
                }
                .joinToString(separator = ", ")
        )
        .side { checkedToast(it) }

fun Fragment.showHintToast(expectedDots: BrailleDots) =
    checkedToast(getString(R.string.input_hint_template).format(expectedDots.spelling))

fun Context.printString(c: Char, mode: PrintMode): String? =
    when (mode) {
        PrintMode.INPUT -> inputSymbolPrintRules[c]
        PrintMode.SHOW -> showSymbolPrintRules[c]
    }

fun Context.printStringNotNullLogged(c: Char, mode: PrintMode): String =
    printString(c, mode) ?: "".also { Timber.e("Intro should be available") }

fun Fragment.printString(c: Char, mode: PrintMode): String? =
    (context ?: null.also { Timber.w("Context is not available") })
        ?.run { printString(c, mode) }

fun Fragment.printStringNotNullLogged(c: Char, mode: PrintMode): String =
    context
        ?.printStringNotNullLogged(c, mode)
        ?: error("Context is not available")