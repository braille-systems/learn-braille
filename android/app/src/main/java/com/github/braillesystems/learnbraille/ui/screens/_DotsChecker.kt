package com.github.braillesystems.learnbraille.ui.screens

import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.entities.spelling
import com.github.braillesystems.learnbraille.toast

fun Fragment.makeCorrectToast(): Unit = toast(getString(R.string.input_correct))

fun Fragment.makeIntroLetterToast(
    toInput: String?
): Unit = toast(
    if (toInput == null) getString(R.string.input_loading)
    else getString(R.string.input_letter_intro_template).format(toInput)
)

fun Fragment.makeIncorrectToast(): Unit = toast(getString(R.string.input_incorrect))

fun Fragment.makeIncorrectLetterToast(letter: String): Unit = toast(
    getString(R.string.input_letter_incorrect_template).format(letter)
)

fun Fragment.makeHintDotsToast(expectedDots: BrailleDots) =
    toast(getString(R.string.input_dots_hint_template).format(expectedDots.spelling))
