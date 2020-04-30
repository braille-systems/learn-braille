package com.github.braillesystems.learnbraille.ui.screens

import android.os.Vibrator
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.github.braillesystems.learnbraille.CORRECT_BUZZ_PATTERN
import com.github.braillesystems.learnbraille.INCORRECT_BUZZ_PATTERN
import com.github.braillesystems.learnbraille.checkedBuzz
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.ui.serial.UsbSerial
import com.github.braillesystems.learnbraille.ui.views.*
import timber.log.Timber

/**
 * Represents state machine that serves input tasks.
 */
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

    fun onCheck()
    fun onHint()

    enum class State {
        INPUT, HINT
    }
}

interface MutableDotsChecker : DotsChecker {

    var getEnteredDots: () -> BrailleDots
    var getExpectedDots: () -> BrailleDots?

    var onCheckHandler: () -> Unit
    var onCorrectHandler: () -> Unit
    var onIncorrectHandler: () -> Unit
    var onHintHandler: () -> Unit
    var onPassHintHandler: () -> Unit

    companion object {
        fun create(): MutableDotsChecker = DotsCheckerImpl()
    }
}

/**
 * Initialize lateinit callbacks firstly
 */
private class DotsCheckerImpl : MutableDotsChecker {

    override lateinit var getEnteredDots: () -> BrailleDots
    override lateinit var getExpectedDots: () -> BrailleDots?

    override var onCheckHandler: () -> Unit = {}
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

    override fun onCheck() = onCheckHandler().also {
        if (state == DotsChecker.State.HINT) {
            state = DotsChecker.State.INPUT
            onPassHint()
        } else {
            if (isCorrect) onCorrect()
            else onIncorrect()
        }
    }

    private fun onCorrect() = onCorrectHandler().also {
        _eventCorrect.value = true
    }

    private fun onIncorrect() = onIncorrectHandler().also {
        _eventIncorrect.value = true
    }

    private fun onPassHint() = onPassHintHandler().also {
        _eventPassHint.value = true
    }

    override fun onHint() = onHintHandler().also {
        if (state == DotsChecker.State.HINT) {
            state = DotsChecker.State.INPUT
            onPassHint()
        } else {
            state = DotsChecker.State.HINT
            _eventHint.value = expectedDots
        }
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

inline fun DotsChecker.observeEventCorrect(
    lifecycleOwner: LifecycleOwner,
    dotsState: BrailleDotsState,
    buzzer: Vibrator? = null,
    crossinline block: () -> Unit = {}
): Unit = eventCorrect.observe(
    lifecycleOwner,
    Observer {
        if (!it) return@Observer
        Timber.i("Handle correct")
        buzzer.checkedBuzz(CORRECT_BUZZ_PATTERN)
        dotsState.uncheck()
        block()
        onCorrectComplete()
    }
)

inline fun DotsChecker.observeEventIncorrect(
    lifecycleOwner: LifecycleOwner,
    dotsState: BrailleDotsState,
    buzzer: Vibrator? = null,
    crossinline block: () -> Unit = {}
): Unit = eventIncorrect.observe(
    lifecycleOwner,
    Observer {
        if (!it) return@Observer
        Timber.i("Handle incorrect: entered = ${dotsState.spelling}")
        buzzer.checkedBuzz(INCORRECT_BUZZ_PATTERN)
        dotsState.uncheck()
        block()
        onIncorrectComplete()
    }
)

inline fun DotsChecker.observeEventHint(
    lifecycleOwner: LifecycleOwner,
    dotsState: BrailleDotsState,
    serial: UsbSerial? = null,
    crossinline block: (BrailleDots) -> Unit
): Unit = eventHint.observe(
    lifecycleOwner,
    Observer { expectedDots ->
        if (expectedDots == null) return@Observer
        Timber.i("Handle hint")
        dotsState.display(expectedDots)
        serial?.trySend(expectedDots)
        block(expectedDots)
        onHintComplete()
    }
)

inline fun DotsChecker.observeEventPassHint(
    lifecycleOwner: LifecycleOwner,
    dotsState: BrailleDotsState,
    crossinline block: () -> Unit
): Unit = eventPassHint.observe(
    lifecycleOwner,
    Observer {
        if (!it) return@Observer
        dotsState.uncheck()
        dotsState.clickable(true)
        block()
        onPassHintComplete()
    }
)
