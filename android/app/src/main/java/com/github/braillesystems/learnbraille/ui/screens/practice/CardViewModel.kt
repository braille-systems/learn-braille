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

    private var _symbol = MutableLiveData<String?>()
    val symbol: LiveData<String?>
        get() = _symbol

    var nTries: Int = 0
        private set

    var nCorrect: Int = 0
        private set

    private var expectedDots: BrailleDots? = null

    val job = Job()
    private val uiScope = scope(job)

    init {
        Timber.i("Initialize practice view model")
        initializeCard()

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

    private fun initializeCard() = uiScope.launch {
        val material = practiceRepository.getNextMaterial()
        require(material.data is Symbol)
        material.data.apply {
            _symbol.value = char.toString()
            expectedDots = brailleDots
        }
    }
}
