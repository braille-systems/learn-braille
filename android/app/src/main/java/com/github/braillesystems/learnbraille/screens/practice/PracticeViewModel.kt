package com.github.braillesystems.learnbraille.screens.practice

import android.app.Application
import androidx.lifecycle.*
import com.github.braillesystems.learnbraille.database.entities.BrailleDots
import com.github.braillesystems.learnbraille.database.entities.SymbolDao
import com.github.braillesystems.learnbraille.language
import com.github.braillesystems.learnbraille.screens.DotsChecker
import com.github.braillesystems.learnbraille.screens.MutableDotsChecker
import com.github.braillesystems.learnbraille.util.scope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class PracticeViewModelFactory(
    private val dataSource: SymbolDao,
    private val application: Application,
    private val getEnteredDots: () -> BrailleDots
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(PracticeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            PracticeViewModel(dataSource, application, getEnteredDots) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}

class PracticeViewModel(
    private val database: SymbolDao,
    application: Application,
    private val getEnteredDots: () -> BrailleDots,
    private val dotsChecker: MutableDotsChecker = MutableDotsChecker.create()
) : AndroidViewModel(application),
    DotsChecker by dotsChecker {

    private var _symbol = MutableLiveData<String>()
    val symbol: LiveData<String>
        get() = _symbol

    var nTries: Int = 0
        private set

    var nCorrect: Int = 0
        private set

    private var expectedDots: BrailleDots? = null

    private val job = Job()
    private val uiScope = scope(job)

    init {
        Timber.i("Initialize practice view model")
        initializeCard()

        dotsChecker.apply {
            getEnteredDots = this@PracticeViewModel.getEnteredDots
            getExpectedDots = { expectedDots }
            onCheckHandler = {
                if (dotsChecker.state == DotsChecker.State.INPUT) {
                    nTries++
                }
            }
            onCorrectHandler = {
                initializeCard()
                nCorrect++
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    private fun initializeCard() = uiScope.launch {
        val entry = database.getRandomSymbol(language) ?: error("DB is not initialized")
        _symbol.value = entry.symbol.toString()
        expectedDots = entry.brailleDots
    }
}
