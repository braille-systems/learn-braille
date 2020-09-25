package com.github.braillesystems.learnbraille.ui.screens.practice

import android.app.Application
import androidx.lifecycle.*
import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.data.repository.MutableActionsRepository
import com.github.braillesystems.learnbraille.data.repository.MutablePracticeRepository
import com.github.braillesystems.learnbraille.ui.screens.DotsChecker
import com.github.braillesystems.learnbraille.ui.screens.MutableDotsChecker
import com.github.braillesystems.learnbraille.utils.retryN
import com.github.braillesystems.learnbraille.utils.scope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import java.util.concurrent.ArrayBlockingQueue

class CardViewModelFactory(
    private val practiceRepository: MutablePracticeRepository,
    private val actionsRepository: MutableActionsRepository,
    private val application: Application,
    private val getEnteredDots: () -> BrailleDots
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(CardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            CardViewModel(practiceRepository, actionsRepository, application, getEnteredDots) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}

class CardViewModel(
    private val practiceRepository: MutablePracticeRepository,
    private val actionsRepository: MutableActionsRepository,
    application: Application,
    private val getEnteredDots: () -> BrailleDots,
    private val dotsChecker: MutableDotsChecker = MutableDotsChecker.create()
) : AndroidViewModel(application),
    DotsChecker by dotsChecker {

    private val _symbol = MutableLiveData<MaterialData>()
    val symbol: LiveData<MaterialData> get() = _symbol

    private val _deckTag = MutableLiveData<String?>()
    val deckTag: LiveData<String?> get() = _deckTag

    var nTries: Int = 0
        private set

    var nCorrect: Int = 0
        private set

    private var expectedDots: BrailleDots? = null

    private val job = Job()
    private val uiScope = scope(job)

    private val nSkipMaterials = 2
    private val materialsQueue: Queue<MaterialData> = ArrayBlockingQueue(nSkipMaterials)

    init {
        Timber.i("Initialize practice view model")
        initializeCard(firstTime = true)

        dotsChecker.apply {
            getEnteredDots = this@CardViewModel.getEnteredDots
            getExpectedDots = { expectedDots }
            onCheckHandler = {
                if (dotsChecker.state == DotsChecker.State.INPUT) {
                    nTries++
                }
            }
            onCorrectHandler = {
                initializeCard()
                nCorrect++
                uiScope.launch {
                    actionsRepository.addAction(PracticeSubmission(isCorrect = true))
                }
            }
            onHintHandler = {
                uiScope.launch {
                    actionsRepository.addAction(PracticeHintAction)
                }
            }
            onIncorrectHandler = {
                uiScope.launch {
                    actionsRepository.addAction(PracticeSubmission(isCorrect = false))
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    private fun initializeCard(firstTime: Boolean = false) = uiScope.launch {
        val material = retryN(
            n = 5,
            stop = { it.data !in materialsQueue },
            get = { practiceRepository.getNextMaterialNotNull() }
        ) ?: practiceRepository.getNextMaterialNotNull()

        if (material.data !in materialsQueue) {
            if (materialsQueue.size == nSkipMaterials) materialsQueue.poll()
            materialsQueue.add(material.data)
        }

        material.data.let {
            _symbol.value = it
            expectedDots = when (it) {
                is Symbol -> it.brailleDots
                is MarkerSymbol -> it.brailleDots
            }
        }

        // Should be called after getting material because deck changes automatically
        // if `use only known materials` enabled and previous `currentDeck`
        // became not available.
        if (firstTime) {
            val deck = practiceRepository.getCurrDeck()
            _deckTag.value = deck.tag
        }
    }
}
