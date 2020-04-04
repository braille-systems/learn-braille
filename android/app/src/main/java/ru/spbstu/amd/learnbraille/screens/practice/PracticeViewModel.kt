package ru.spbstu.amd.learnbraille.screens.practice

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import kotlinx.coroutines.*
import ru.spbstu.amd.learnbraille.database.*
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

class PracticeViewModel(
    private val database: SymbolDao,
    application: Application,
    private val dotsState: BrailleDotsState
) : AndroidViewModel(application) {

    private val _backingSymbol = MutableLiveData<String>() // True backing field
    private var _symbol: Char // This class interface to the true backing filed
        get() = _backingSymbol.value!!.first()
        set(value) {
            _backingSymbol.value = value.toString()
        }
    val symbol: LiveData<String>
        get() = _backingSymbol

    private val _backingNLettersFaced = MutableLiveData<Int>()
    private var _nLettersFaced: Int
        get() = _backingNLettersFaced.value ?: error("nLettersFaced should be initialized")
        set(value) {
            _backingNLettersFaced.value = value
        }
    val nLettersFaced: LiveData<Int>
        get() = _backingNLettersFaced

    private val _backingNCorrect = MutableLiveData<Int>()
    private var _nCorrect: Int
        get() = _backingNCorrect.value ?: error("nCorrect should be initialized")
        set(value) {
            _backingNCorrect.value = value
        }
    val nCorrect: LiveData<Int>
        get() = _backingNCorrect

    private val _eventCorrect = MutableLiveData<Boolean>()
    val eventCorrect: LiveData<Boolean>
        get() = _eventCorrect

    private val _eventIncorrect = MutableLiveData<Boolean>()
    val eventIncorrect: LiveData<Boolean>
        get() = _eventIncorrect

    private val _eventWaitDBInit = MutableLiveData<Boolean>()
    val eventWaitDBInit: LiveData<Boolean>
        get() = _eventWaitDBInit

    private var expectedDots: BrailleDots? = null
    private val enteredDots get() = dotsState.brailleDots

    private val isCorrect: Boolean
        get() = (enteredDots == expectedDots).also {
            Timber.i(
                if (it) "Correct: " else "Incorrect: " +
                        "entered = $enteredDots, expected = $expectedDots"
            )
        }
    private var _hintUsed:Boolean = false

    private val language = Language.RU // Temporary field, will move to settings

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    init {
        initializeCard()
        _nLettersFaced = 0
        _nCorrect = 0
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun onNext() {
        _nLettersFaced++
        if (isCorrect) {
            onCorrect()
        } else {
            onIncorrect()
        }
        _hintUsed = false
    }

    fun onHint() {
        _hintUsed = true
    }

    fun ifHintUsed(): Boolean {
        return _hintUsed
    }

    fun onCorrectComplete() {
        _eventCorrect.value = false
    }

    fun onIncorrectComplete() {
        _eventIncorrect.value = false
    }

    fun onEventWaitDBInitComplete() {
        _eventWaitDBInit.value = false
    }

    fun getDotsString(): String? {
        var res = "Ответ: точки "
        if(expectedDots?.b1 == BrailleDot.F)
            res += "1 "
        if(expectedDots?.b2 == BrailleDot.F)
            res += "2 "
        if(expectedDots?.b3 == BrailleDot.F)
            res += "3 "
        if(expectedDots?.b4 == BrailleDot.F)
            res += "4 "
        if(expectedDots?.b5 == BrailleDot.F)
            res += "5 "
        if(expectedDots?.b6 == BrailleDot.F)
            res += "6"
        return res
    }

    fun getExpectedDots() = expectedDots

    private fun onCorrect() = initializeCard().also {
        if (!_hintUsed)
            _nCorrect++
        _eventCorrect.value = true
    }

    private fun onIncorrect() {
        _eventIncorrect.value = true
    }

    private fun onWaitDBInit() {
        _eventWaitDBInit.value = true
    }

    private fun initializeCard() = uiScope.launch {
        val entry = getEntryFromDatabase(language) ?: onWaitDBInit().run { return@launch }
        _symbol = entry.symbol
        expectedDots = entry.brailleDots
    }

    private suspend fun getEntryFromDatabase(language: Language) = withContext(Dispatchers.IO) {
        database.getRandomSymbol(language)
    }
}
