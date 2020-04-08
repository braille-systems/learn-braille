package ru.spbstu.amd.learnbraille.screens

import android.os.Vibrator
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import ru.spbstu.amd.learnbraille.CORRECT_BUZZ_PATTERN
import ru.spbstu.amd.learnbraille.INCORRECT_BUZZ_PATTERN
import ru.spbstu.amd.learnbraille.database.entities.BrailleDots
import ru.spbstu.amd.learnbraille.serial.UsbSerial
import ru.spbstu.amd.learnbraille.side
import ru.spbstu.amd.learnbraille.views.Dots
import ru.spbstu.amd.learnbraille.views.clickable
import ru.spbstu.amd.learnbraille.views.display
import ru.spbstu.amd.learnbraille.views.uncheck
import timber.log.Timber

interface DotsChecker {

    val eventCorrect: LiveData<Boolean>
    fun onCorrectComplete()

    val eventIncorrect: LiveData<Boolean>
    fun onIncorrectComplete()

    val eventHint: LiveData<BrailleDots?>
    fun onHintComplete()

    val eventPassHint: LiveData<Boolean>
    fun onPassHintComplete()

    val state: State

    fun onNext()
    fun onHint()

    enum class State {
        INPUT, HINT
    }
}

interface MutableDotsChecker : DotsChecker {

    var getEnteredDots: () -> BrailleDots
    var getExpectedDots: () -> BrailleDots?

    var onNextHandler: () -> Unit
    var onCorrectHandler: () -> Unit
    var onIncorrectHandler: () -> Unit
    var onHintHandler: () -> Unit
    var onPassHintHandler: () -> Unit

    companion object {
        fun create(): MutableDotsChecker = DotsCheckerImpl()
    }
}

/**
 * Initialize callbacks firstly
 */
private class DotsCheckerImpl : MutableDotsChecker {

    override lateinit var getEnteredDots: () -> BrailleDots
    override lateinit var getExpectedDots: () -> BrailleDots?

    override var onNextHandler: () -> Unit = {}
    override var onCorrectHandler: () -> Unit = {}
    override var onIncorrectHandler: () -> Unit = {}
    override var onHintHandler: () -> Unit = {}
    override var onPassHintHandler: () -> Unit = {}

    private val _eventCorrect = MutableLiveData<Boolean>()
    override val eventCorrect: LiveData<Boolean>
        get() = _eventCorrect

    private val _eventIncorrect = MutableLiveData<Boolean>()
    override val eventIncorrect: LiveData<Boolean>
        get() = _eventIncorrect

    private val _eventHint = MutableLiveData<BrailleDots>()
    override val eventHint: LiveData<BrailleDots?>
        get() = _eventHint

    private val _eventPassHint = MutableLiveData<Boolean>()
    override val eventPassHint: LiveData<Boolean>
        get() = _eventPassHint

    private val enteredDots: BrailleDots
        get() =
            if (::getEnteredDots.isInitialized) getEnteredDots()
            else error("getEnteredDots property should be initialized")

    private val expectedDots: BrailleDots?
        get() =
            if (::getExpectedDots.isInitialized) getExpectedDots()
            else error("getExpectedDots should be initialized")

    private val isCorrect: Boolean
        get() = (enteredDots == expectedDots).also {
            Timber.i(
                if (it) "Correct: " else "Incorrect: " +
                        "entered = ${enteredDots}, expected = $expectedDots"
            )
        }

    override var state = DotsChecker.State.INPUT
        private set

    override fun onNext() = onNextHandler().side {
        if (state == DotsChecker.State.HINT) {
            state = DotsChecker.State.INPUT
            onPassHist()
        } else {
            if (isCorrect) onCorrect()
            else onIncorrect()
        }
    }

    private fun onCorrect() = onCorrectHandler().side {
        _eventCorrect.value = true
    }

    private fun onIncorrect() = onIncorrectHandler().side {
        _eventIncorrect.value = true
    }

    private fun onPassHist() = onPassHintHandler().side {
        _eventPassHint.value = true
    }

    override fun onHint() = onHintHandler().side {
        state = DotsChecker.State.HINT
        _eventHint.value = expectedDots
    }

    override fun onCorrectComplete() {
        _eventCorrect.value = false
    }

    override fun onHintComplete() {
        _eventHint.value = null
    }

    override fun onPassHintComplete() {
        _eventPassHint.value = false
    }

    override fun onIncorrectComplete() {
        _eventIncorrect.value = false
    }
}

fun DotsChecker.getEventCorrectObserver(
    dots: Dots,
    buzzer: Vibrator? = null,
    f: () -> Unit = {}
) = Observer<Boolean> {
    if (!it) return@Observer
    buzzer.buzz(CORRECT_BUZZ_PATTERN)
    dots.uncheck()
    f()
    onCorrectComplete()
}

fun DotsChecker.getEventIncorrectObserver(
    dots: Dots,
    buzzer: Vibrator? = null,
    f: () -> Unit = {}
) = Observer<Boolean> {
    if (!it) return@Observer
    buzzer.buzz(INCORRECT_BUZZ_PATTERN)
    dots.uncheck()
    f()
    onIncorrectComplete()
}

fun DotsChecker.getEventHintObserver(
    dots: Dots,
    serial: UsbSerial? = null,
    f: (BrailleDots) -> Unit
) = Observer<BrailleDots?> { expectedDots ->
    if (expectedDots == null) return@Observer
    dots.display(expectedDots)
    dots.clickable(false)
    serial?.trySend(expectedDots)
    f(expectedDots)
    onHintComplete()
}

fun DotsChecker.getEventPassHintObserver(
    dots: Dots,
    f: () -> Unit
) = Observer<Boolean> {
    if (!it) return@Observer
    dots.uncheck()
    dots.clickable(true)
    f()
    onPassHintComplete()
}
