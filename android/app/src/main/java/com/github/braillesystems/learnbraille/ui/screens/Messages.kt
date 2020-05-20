package com.github.braillesystems.learnbraille.ui.screens

import android.content.Context
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.entities.Material
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.data.entities.spelling
import com.github.braillesystems.learnbraille.res.symbolTypeIntroList
import com.github.braillesystems.learnbraille.utils.checkedToast
import com.github.braillesystems.learnbraille.utils.peek
import com.github.braillesystems.learnbraille.utils.toast
import timber.log.Timber

fun Fragment.showCorrectToast(): Unit = toast(getString(R.string.input_correct))

fun Fragment.showIncorrectToast(material: Material? = null): Unit =
    if (material == null) toast(getString(R.string.input_incorrect))
    else toast("${getString(R.string.input_incorrect)} ${introString(material).orEmpty()}")

fun Fragment.showHintDotsToast(expectedDots: BrailleDots) =
    checkedToast(getString(R.string.input_dots_hint_template).format(expectedDots.spelling))

fun Context.introString(material: Material): String? =
    when (material.data) {
        is Symbol -> symbolTypeIntroList.peek(material.data.char)
    }

fun Context.introStringNotNullLogged(material: Material): String = introString(material)
    ?: Timber.e("Intro should be available").let { "" }

fun Fragment.introString(material: Material): String? =
    (context ?: null.also { Timber.w("Context is not available") })
        ?.run { introString(material) }

fun Fragment.introStringNotNullLogged(material: Material): String =
    context?.introStringNotNullLogged(material) ?: error("Context is not available")
