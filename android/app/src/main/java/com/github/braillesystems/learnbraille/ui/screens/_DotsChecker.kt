package com.github.braillesystems.learnbraille.ui.screens

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.entities.spelling
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import org.koin.android.ext.android.get

// TODO remove?
// TODO preference check if toasts enabled

fun Fragment.makeCorrectToast(preferenceRepository: PreferenceRepository = get()): Unit =
    Toast.makeText(
        context, getString(R.string.input_correct),
        preferenceRepository.toastDuration
    ).show()

fun Fragment.makeIntroLetterToast(
    toInput: String?,
    preferenceRepository: PreferenceRepository = get()
): Unit =
    Toast.makeText(
        context,
        if (toInput == null) getString(R.string.input_loading)
        else getString(R.string.input_letter_intro_template).format(toInput),
        preferenceRepository.toastDuration
    ).show()

fun Fragment.makeIncorrectToast(preferenceRepository: PreferenceRepository = get()): Unit =
    Toast.makeText(
        context,
        getString(R.string.input_incorrect),
        preferenceRepository.toastDuration
    ).show()

fun Fragment.makeIncorrectLetterToast(
    letter: String?,
    preferenceRepository: PreferenceRepository = get()
): Unit =
    Toast.makeText(
        context,
        if (letter == null) getString(R.string.input_loading)
        else getString(R.string.input_letter_incorrect_template).format(letter),
        preferenceRepository.toastDuration
    ).show()

fun Fragment.makeHintDotsToast(
    expectedDots: BrailleDots,
    preferenceRepository: PreferenceRepository = get()
): Unit =
    Toast.makeText(
        context,
        getString(R.string.input_dots_hint_template).format(expectedDots.spelling),
        preferenceRepository.toastDuration
    ).show()
