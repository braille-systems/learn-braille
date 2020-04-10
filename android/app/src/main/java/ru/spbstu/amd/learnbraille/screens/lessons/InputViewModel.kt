package ru.spbstu.amd.learnbraille.screens.lessons

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import ru.spbstu.amd.learnbraille.database.entities.BrailleDots
import ru.spbstu.amd.learnbraille.screens.DotsChecker
import ru.spbstu.amd.learnbraille.screens.MutableDotsChecker
import timber.log.Timber

class InputViewModelFactory(
    private val application: Application,
    private val expectedDots: BrailleDots,
    private val getEnteredDots: () -> BrailleDots
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(InputViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            InputViewModel(application, getEnteredDots, expectedDots) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}

class InputViewModel(
    application: Application,
    getEnteredDots: () -> BrailleDots,
    private val expectedDots: BrailleDots,
    private val dotsChecker: MutableDotsChecker = MutableDotsChecker.create()
) : AndroidViewModel(application),
    DotsChecker by dotsChecker {

    init {
        Timber.i("Initialize input view model")
        dotsChecker.getEnteredDots = getEnteredDots
        dotsChecker.getExpectedDots = { expectedDots }
    }
}
