package com.github.braillesystems.learnbraille.ui.screens

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.TOAST_DURATION
import com.github.braillesystems.learnbraille.data.types.BrailleDots
import com.github.braillesystems.learnbraille.data.types.spelling

// TODO is needed?

fun Fragment.makeCorrectToast(): Unit =
    Toast.makeText(
        context, getString(R.string.input_correct), TOAST_DURATION
    ).show()

fun Fragment.makeIntroLetterToast(toInput: String?): Unit =
    Toast.makeText(
        context,
        if (toInput == null) getString(R.string.input_loading)
        else getString(R.string.input_letter_intro_template).format(toInput),
        TOAST_DURATION
    ).show()

fun Fragment.makeIncorrectToast(): Unit =
    Toast.makeText(
        context,
        getString(R.string.input_incorrect),
        TOAST_DURATION
    ).show()

fun Fragment.makeIncorrectLetterToast(letter: String?): Unit =
    Toast.makeText(
        context,
        if (letter == null) getString(R.string.input_loading)
        else getString(R.string.input_letter_incorrect_template).format(letter),
        TOAST_DURATION
    ).show()

fun Fragment.makeHintDotsToast(expectedDots: BrailleDots): Unit =
    Toast.makeText(
        context,
        getString(R.string.input_dots_hint_template).format(expectedDots.spelling),
        TOAST_DURATION
    ).show()
