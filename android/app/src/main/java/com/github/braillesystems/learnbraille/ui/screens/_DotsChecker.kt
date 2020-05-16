package com.github.braillesystems.learnbraille.ui.screens

import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.entities.Material
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.data.entities.spelling
import com.github.braillesystems.learnbraille.res.ruSymbols
import com.github.braillesystems.learnbraille.res.specialSymbols
import com.github.braillesystems.learnbraille.res.uebDigits
import com.github.braillesystems.learnbraille.utils.*
import timber.log.Timber

fun Fragment.showCorrectToast(): Unit = toast(getString(R.string.input_correct))

fun Fragment.showIncorrectToast(material: Material? = null): Unit =
    if (material == null) toast(getString(R.string.input_incorrect))
    else toast("${getString(R.string.input_incorrect)} ${introString(material).orEmpty()}")

fun Fragment.showHintDotsToast(expectedDots: BrailleDots) =
    checkedToast(getString(R.string.input_dots_hint_template).format(expectedDots.spelling))

fun Fragment.introString(material: Material): String? =
    when (material.data) {
        is Symbol -> symbolsRuleList.peek(material.data.symbol)
    }

fun Fragment.introStringNotNullLogged(material: Material): String = introString(material)
    ?: Timber.e("Intro should be available").let { "" }

/**
 * Add here rules, how to display hints for symbols.
 */
private val Fragment.symbolsRuleList: List<P2F<Char, String>> by lazyWithContext {
    listOfP2F(
        // Prevent lambda of capturing context that will be invalid next time fragment entered
        getString(R.string.input_letter_intro_template).let {
            { c: Char -> ruSymbols.map.containsKey(c) } to { c: Char -> it.format(c) }
        },
        getString(R.string.input_digit_intro_template).let {
            { c: Char -> uebDigits.map.containsKey(c) } to { c: Char -> it.format(c) }
        },
        getString(R.string.input_special_intro_template).let {
            { c: Char -> specialSymbols.map.containsKey(c) } to { c: Char -> it.format(c) }
        }
    )
}
