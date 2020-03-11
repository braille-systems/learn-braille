package ru.spbstu.amd.learnbraille.screens.practice

import android.app.Application
import android.widget.CheckBox
import androidx.lifecycle.*
import kotlinx.coroutines.*
import ru.spbstu.amd.learnbraille.database.BrailleDots
import ru.spbstu.amd.learnbraille.database.Language
import ru.spbstu.amd.learnbraille.database.SymbolDao
import timber.log.Timber

data class TryAgainData(val letter: Char, val brailleDots: BrailleDots)

class PracticeViewModelFactory(
    private val dataSource: SymbolDao,
    private val application: Application,
    private val dotCheckBoxes: Array<CheckBox>,
    private val tryAgainData: TryAgainData? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(PracticeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            PracticeViewModel(dataSource, application, dotCheckBoxes, tryAgainData) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}

class PracticeViewModel(
    private val database: SymbolDao,
    application: Application,
    private val dotCheckBoxes: Array<CheckBox>,
    tryAgainData: TryAgainData?
) : AndroidViewModel(application) {

    private val _backingLetter = MutableLiveData<String>() // True backing field
    private var _letter: Char // This class interface to the true backing filed
        get() = _backingLetter.value!!.first()
        set(value) {
            _backingLetter.value = value.toString()
        }
    val letter: LiveData<String>
        get() = _backingLetter

    private val _eventCorrect = MutableLiveData<Boolean>()
    val eventCorrect: LiveData<Boolean>
        get() = _eventCorrect

    private val _eventIncorrect = MutableLiveData<Boolean>()
    val eventIncorrect: LiveData<Boolean>
        get() = _eventIncorrect

    var expectedDots: BrailleDots? = null
    private val enteredDots
        get() = BrailleDots(dotCheckBoxes.map { it.isChecked }.toBooleanArray())

    private val isCorrect: Boolean
        get() {
            val res = enteredDots == expectedDots
            Timber.i(
                if (res) "Correct: " else "Incorrect: " +
                        "entered = $enteredDots, expected = $expectedDots"
            )
            return res
        }

    private val language = Language.RU

    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    init {
        initializeLetter(tryAgainData)
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun onNext() {
        if (isCorrect) {
            _eventCorrect.value = true
        } else {
            _eventIncorrect.value = true
        }
    }

    fun onCorrectComplete() {
        _eventCorrect.value = false
    }

    fun onIncorrectComplete() {
        _eventIncorrect.value = false
    }

    private fun initializeLetter(tryAgainData: TryAgainData?) = uiScope.launch {
        if (tryAgainData != null) {
            _letter = tryAgainData.letter
            expectedDots = tryAgainData.brailleDots
        } else {
            val entry = getEntryFromDatabase(language)
                ?: throw IllegalStateException("No letters in database")
            _letter = entry.symbol
            expectedDots = entry.brailleDots
        }
    }

    private suspend fun getEntryFromDatabase(language: Language) = withContext(Dispatchers.IO) {
        database.getRandomEntry(language)
    }
}
