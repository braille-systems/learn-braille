package ru.spbstu.amd.learnbraille.screens.lessons

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.*
import ru.spbstu.amd.learnbraille.database.entities.*
import timber.log.Timber

class LessonStepViewModelFactory(
    private val application: Application,
    private val userId: Long,
    private val stepDao: StepDao,
    private val userPassedStepDao: UserPassedStepDao,
    private val dotsStateGetter: () -> BrailleDotsState?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(LessonStepViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            LessonStepViewModel(
                application, userId,
                stepDao, userPassedStepDao,
                dotsStateGetter
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}

class LessonStepViewModel(
    application: Application,
    private val userId: Long,
    private val stepDao: StepDao,
    private val userPassedStepDao: UserPassedStepDao,
    private val dotsStateGetter: () -> BrailleDotsState?
) : AndroidViewModel(application) {

    private val _backingCurrentLessonStep = MutableLiveData<LessonWithStep>()
    private var _currentLessonStep: LessonWithStep
        get() = _backingCurrentLessonStep.value ?: error("Current step should be initialized")
        set(value) {
            _backingCurrentLessonStep.value = value
        }
    val currentLessonStep: LiveData<LessonWithStep>
        get() = _backingCurrentLessonStep

    private val _eventCorrect = MutableLiveData<Boolean>()
    val eventCorrect: LiveData<Boolean>
        get() = _eventCorrect

    private val _eventIncorrect = MutableLiveData<Boolean>()
    val eventIncorrect: LiveData<Boolean>
        get() = _eventIncorrect

    private var stepsPassedUntil = 0L

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    init {
        initializeCurrentStep()
    }

    private fun initializeCurrentStep() = uiScope.launch {
        _currentLessonStep = getCurrentStepFromDatabase(userId)
            ?: error("User should always have at least one not passed step")
        stepsPassedUntil = _currentLessonStep.step.id
    }

    private suspend fun getCurrentStepFromDatabase(userId: Long) = withContext(Dispatchers.IO) {
        stepDao.getCurrentStepForUser(userId)
    }

    fun prevStep(): Unit = setPrevStep()

    fun nextStep(): Unit = if (_currentLessonStep.step.id < stepsPassedUntil) {
        setNextStep()
    } else {
        when (val data = _currentLessonStep.step.data) {
            is LastInfo -> Timber.w("No next step: last step reached, remove next button from ui")
            is InputSymbol -> checkInput(data.symbol.brailleDots)
            is InputDots -> checkInput(data.dots)
            else -> {
                markPassed()
                setNextStep()
            }
        }
    }

    private fun checkInput(input: BrailleDots) = dotsStateGetter()
        ?.brailleDots
        ?.equals(input)
        ?.let { if (it) onCorrect() else onIncorrect() }
        ?: error("Dots state is needed to check input correctness")

    private fun onCorrect() {
        markPassed()
        _eventCorrect.value = true
    }

    private fun onIncorrect() {
        _eventIncorrect.value = true
    }

    fun onCorrectComplete() {
        _eventCorrect.value = false
    }

    fun onIncorrectComplete() {
        _eventIncorrect.value = false
    }

    private fun markPassed() = uiScope.launch {
        insertPassedStepToDatabase()
        stepsPassedUntil = _currentLessonStep.step.id
    }

    private suspend fun insertPassedStepToDatabase() = withContext(Dispatchers.IO) {
        userPassedStepDao.insertPassedStep(
            UserPassedStep(
                userId,
                _currentLessonStep.step.id
            )
        )
    }

    private fun setStep(id: Long) = uiScope.launch {
        _currentLessonStep = getStepFromDatabase(id) ?: return@launch
    }

    private suspend fun getStepFromDatabase(id: Long) = withContext(Dispatchers.IO) {
        stepDao.getStep(id)
    }

    private fun setNextStep() {
        setStep(_currentLessonStep.step.id + 1)
    }

    private fun setPrevStep() {
        setStep(_currentLessonStep.step.id - 1)
    }
}
