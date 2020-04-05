package ru.spbstu.amd.learnbraille.screens.practice

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.*
import ru.spbstu.amd.learnbraille.database.BrailleDots
import ru.spbstu.amd.learnbraille.database.BrailleDotsState
import ru.spbstu.amd.learnbraille.database.Language
import ru.spbstu.amd.learnbraille.database.SymbolDao
import ru.spbstu.amd.learnbraille.language
import ru.spbstu.amd.learnbraille.side
import timber.log.Timber

class PracticeViewModelFactory(
    private val dataSource: SymbolDao,
    private val application: Application,
    private val dotsState: BrailleDotsState
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(PracticeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            PracticeViewModel(dataSource, application, dotsState) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}

/**
 * Exposes live data:
 * - symbol
 * - nLettersFaced
 * - nCorrect
 *
 * Events:
 * - eventCorrect
 * - eventIncorrect
 * - eventHint
 * - eventPassHint
 */
class PracticeViewModel(
    private val database: SymbolDao,
    application: Application,
    private val dotsState: BrailleDotsState
) : AndroidViewModel(application) {

    private var _symbol = MutableLiveData<String>()
    val symbol: LiveData<String>
        get() = _symbol

    var nTries: Int = 0
        private set

    var nCorrect: Int = 0
        private set

    private val _eventCorrect = MutableLiveData<Boolean>()
    val eventCorrect: LiveData<Boolean>
        get() = _eventCorrect

    private val _eventIncorrect = MutableLiveData<Boolean>()
    val eventIncorrect: LiveData<Boolean>
        get() = _eventIncorrect

    private val _eventHint = MutableLiveData<BrailleDots>()
    val eventHint: LiveData<BrailleDots>
        get() = _eventHint

    private val _eventPassHint = MutableLiveData<Boolean>()
    val eventPassHint: LiveData<Boolean>
        get() = _eventPassHint

    private var expectedDots: BrailleDots? = null
    private val enteredDots get() = dotsState.brailleDots

    private val isCorrect: Boolean
        get() = (enteredDots == expectedDots).also {
            Timber.i(
                if (it) "Correct: " else "Incorrect: " +
                        "entered = $enteredDots, expected = $expectedDots"
            )
        }

    // TODO unify for app
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private var state = State.INPUT

    init {
        initializeCard()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun onNext(): Unit =
        if (state == State.HINT) {
            state = State.INPUT
            onPassHist()
        } else {
            nTries++
            if (isCorrect) {
                onCorrect()
            } else {
                onIncorrect()
            }
        }

    private fun onCorrect() = initializeCard().side {
        nCorrect++
        _eventCorrect.value = true
    }

    private fun onIncorrect() {
        _eventIncorrect.value = true
    }

    private fun onPassHist() {
        _eventPassHint.value = true
    }

    fun onHint() {
        state = State.HINT
        _eventHint.value = expectedDots
    }

    fun onCorrectComplete() {
        _eventCorrect.value = false
    }

    fun onHintComplete() {
        _eventHint.value = null
    }

    fun onPassHintComplete() {
        _eventPassHint.value = false
    }

    fun onIncorrectComplete() {
        _eventIncorrect.value = false
    }

    private fun initializeCard() = uiScope.launch {
        val entry = getEntryFromDatabase(language) ?: error("DB is not initialized")
        _symbol.value = entry.symbol.toString()
        expectedDots = entry.brailleDots
    }

    // TODO unify context for app
    private suspend fun getEntryFromDatabase(language: Language) = withContext(Dispatchers.IO) {
        database.getRandomSymbol(language)
    }

    private enum class State {
        INPUT, HINT
    }
}
