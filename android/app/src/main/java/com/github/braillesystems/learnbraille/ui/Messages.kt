package com.github.braillesystems.learnbraille.ui

import android.content.Context
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.entities.Material
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.data.entities.spelling
import com.github.braillesystems.learnbraille.res.SymbolTypeIntro
import com.github.braillesystems.learnbraille.utils.checkedToast
import com.github.braillesystems.learnbraille.utils.peek
import com.github.braillesystems.learnbraille.utils.toast
import timber.log.Timber

enum class IntroMode {
    INPUT, SHOW
}

fun Fragment.showCorrectToast(): Unit = toast(getString(R.string.input_correct))

fun Fragment.showIncorrectToast(material: Material? = null): Unit =
    if (material == null) toast(getString(R.string.input_incorrect))
    else toast(
        "${getString(R.string.input_incorrect)} " +
                introString(material, IntroMode.INPUT).orEmpty()
    )

fun Fragment.showHintDotsToast(expectedDots: BrailleDots) =
    checkedToast(getString(R.string.input_dots_hint_template).format(expectedDots.spelling))

fun Context.introString(material: Material, mode: IntroMode): String? =
    when (material.data) {
        is Symbol -> SymbolTypeIntro.run {
            when (mode) {
                IntroMode.INPUT -> input.peek(material.data.char)
                IntroMode.SHOW -> show.peek(material.data.char)
            }
        }
    }

fun Context.introStringNotNullLogged(material: Material, mode: IntroMode): String =
    introString(material, mode) ?: "".also { Timber.e("Intro should be available") }

fun Fragment.introString(material: Material, mode: IntroMode): String? =
    (context ?: null.also { Timber.w("Context is not available") })
        ?.run { introString(material, mode) }

fun Fragment.introStringNotNullLogged(material: Material, mode: IntroMode): String =
    context?.introStringNotNullLogged(material, mode) ?: error("Context is not available")
