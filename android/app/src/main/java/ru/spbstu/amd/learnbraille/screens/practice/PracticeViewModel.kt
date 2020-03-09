package ru.spbstu.amd.learnbraille.screens.practice

import android.widget.CheckBox
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.spbstu.amd.learnbraille.screens.practice.BrailleDot.E
import ru.spbstu.amd.learnbraille.screens.practice.BrailleDot.F

@Suppress("UNCHECKED_CAST")
class PracticeViewModelFactory(
    private val tryAgainLetter: Char? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PracticeViewModel::class.java)) {
            return PracticeViewModel(tryAgainLetter) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class PracticeViewModel(tryAgainLetter: Char? = null) : ViewModel() {

    companion object {
        private val dotsToRuLetters = mapOf(
            BrailleDots(F, E, E, E, E, E) to 'А',
            BrailleDots(F, F, E, E, E, E) to 'Б',
            BrailleDots(E, F, E, F, F, F) to 'В',
            BrailleDots(F, F, E, F, F, E) to 'Г',
            BrailleDots(F, E, E, F, F, E) to 'Д',
            BrailleDots(F, E, E, E, F, E) to 'Е',
            BrailleDots(F, E, E, E, E, F) to 'Ё',
            BrailleDots(E, F, E, F, F, E) to 'Ж',
            BrailleDots(F, E, F, E, F, F) to 'З',
            BrailleDots(E, F, E, F, E, E) to 'И',
            BrailleDots(F, F, F, F, E, F) to 'Й',
            BrailleDots(F, E, F, E, E, E) to 'К',
            BrailleDots(F, F, F, E, E, E) to 'Л',
            BrailleDots(F, E, F, F, E, E) to 'М',
            BrailleDots(F, E, F, F, F, E) to 'Н',
            BrailleDots(F, E, F, E, F, E) to 'О',
            BrailleDots(F, F, F, F, E, E) to 'П',
            BrailleDots(F, F, F, E, F, E) to 'Р',
            BrailleDots(E, F, F, F, E, E) to 'С',
            BrailleDots(E, F, F, F, F, E) to 'Т',
            BrailleDots(F, E, F, E, E, F) to 'У',
            BrailleDots(F, F, E, F, E, E) to 'Ф',
            BrailleDots(F, F, E, E, F, E) to 'Х',
            BrailleDots(F, E, E, F, E, E) to 'Ц',
            BrailleDots(F, F, F, F, F, E) to 'Ч',
            BrailleDots(F, E, E, E, F, F) to 'Ш',
            BrailleDots(F, E, F, F, E, F) to 'Щ',
            BrailleDots(F, F, F, E, F, F) to 'Ъ',
            BrailleDots(E, F, F, F, E, F) to 'Ы',
            BrailleDots(E, F, F, F, F, F) to 'Ь',
            BrailleDots(E, F, E, F, E, F) to 'Э',
            BrailleDots(F, F, E, E, F, F) to 'Ю',
            BrailleDots(F, F, E, F, E, F) to 'Я'
        )
    }

    private val enteredDots
        get() = BrailleDots(dotCheckBoxes.map { it.isChecked })

    private val isCorrect
        get() = _letter == dotsToRuLetters[enteredDots]

    private val _backingLetter = MutableLiveData<String>()
    private var _letter: Char
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

    // Need to be initialized after binding to the fragment
    lateinit var dotCheckBoxes: Array<CheckBox>

    init {
        assert(dotsToRuLetters.size == 33) {
            "33 letters are in russian"
        }
        _letter = tryAgainLetter ?: randomRuLetter()
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

    private fun randomRuLetter() = dotsToRuLetters.values.random()
}

enum class BrailleDot {
    E,  // Empty
    F;  // Filled

    companion object {
        fun valueOf(b: Boolean) = if (b) F else E
    }
}

data class BrailleDots(
    val b1: BrailleDot = E, val b2: BrailleDot = E, val b3: BrailleDot = E,
    val b4: BrailleDot = E, val b5: BrailleDot = E, val b6: BrailleDot = E
) {
    constructor(dots: List<Boolean>) : this(
        b1 = BrailleDot.valueOf(dots[0]),
        b2 = BrailleDot.valueOf(dots[1]),
        b3 = BrailleDot.valueOf(dots[2]),
        b4 = BrailleDot.valueOf(dots[3]),
        b5 = BrailleDot.valueOf(dots[4]),
        b6 = BrailleDot.valueOf(dots[5])
    ) {
        require(dots.size == 6) {
            "Only 6 dots Braille system supported"
        }
    }
}
