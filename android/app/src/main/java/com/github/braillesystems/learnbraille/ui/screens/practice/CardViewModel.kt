package com.github.braillesystems.learnbraille.ui.screens.practice

import android.app.Application
import androidx.lifecycle.*
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.data.repository.PracticeRepository
import com.github.braillesystems.learnbraille.ui.screens.DotsChecker
import com.github.braillesystems.learnbraille.ui.screens.MutableDotsChecker
import com.github.braillesystems.learnbraille.utils.scope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class CardViewModelFactory(
    private val practiceRepository: PracticeRepository,
    private val application: Application,
    private val getEnteredDots: () -> BrailleDots
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(CardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            CardViewModel(practiceRepository, application, getEnteredDots) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}

class CardViewModel(
    private val practiceRepository: PracticeRepository,
    application: Application,
    private val getEnteredDots: () -> BrailleDots,
    private val dotsChecker: MutableDotsChecker = MutableDotsChecker.create()
) : AndroidViewModel(application),
    DotsChecker by dotsChecker {

    private val _symbol = MutableLiveData<String?>()
    val symbol: LiveData<String?> get() = _symbol

    private val _deckTag = MutableLiveData<String?>()
    val deckTag: LiveData<String?> get() = _deckTag

    var nTries: Int = 0
        private set

    var nCorrect: Int = 0
        private set

    private var expectedDots: BrailleDots? = null

    val job = Job()
    private val uiScope = scope(job)

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
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    private fun initializeCard(firstTime: Boolean = false) = uiScope.launch {
        val material = practiceRepository.getNextMaterial()
        require(material.data is Symbol)
        material.data.apply {
            _symbol.value = char.toString()
            expectedDots = brailleDots
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
