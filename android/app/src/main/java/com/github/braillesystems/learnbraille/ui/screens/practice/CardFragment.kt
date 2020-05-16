package com.github.braillesystems.learnbraille.ui.screens.practice

import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.dummyMaterialOf
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import com.github.braillesystems.learnbraille.databinding.FragmentPracticeBinding
import com.github.braillesystems.learnbraille.ui.brailletrainer.BrailleTrainer
import com.github.braillesystems.learnbraille.ui.brailletrainer.BrailleTrainerSignalHandler
import com.github.braillesystems.learnbraille.ui.screens.*
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsState
import com.github.braillesystems.learnbraille.ui.views.brailleDots
import com.github.braillesystems.learnbraille.ui.views.dotsState
import com.github.braillesystems.learnbraille.utils.announceByAccessibility
import com.github.braillesystems.learnbraille.utils.checkedToast
import com.github.braillesystems.learnbraille.utils.updateTitle
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class CardFragment : AbstractFragmentWithHelp(R.string.practice_help) {

    private val preferences: PreferenceRepository by inject()

    private lateinit var viewModel: CardViewModel
    private lateinit var dotsState: BrailleDotsState
    private var buzzer: Vibrator? = null

    private val title: String
        get() = getString(R.string.practice_actionbar_title_template).let {
            if (::viewModel.isInitialized) it.format(
                viewModel.nCorrect,
                viewModel.nTries
            )
            else it.format(0, 0)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentPracticeBinding>(
        inflater,
        R.layout.fragment_practice,
        container,
        false
    ).apply {

        Timber.i("onCreateView")

        updateTitle(title)
        setHasOptionsMenu(true)

        dotsState = brailleDots.dotsState

        val viewModelFactory: CardViewModelFactory by inject {
            parametersOf({ dotsState.brailleDots })
        }
        viewModel = ViewModelProvider(
            this@CardFragment, viewModelFactory
        ).get(CardViewModel::class.java)
        buzzer = activity?.getSystemService()
        BrailleTrainer.setSignalHandler(object : BrailleTrainerSignalHandler {
            override fun onJoystickRight() = viewModel.onCheck()
            override fun onJoystickLeft() = viewModel.onHint()
        })


        cardViewModel = viewModel
        lifecycleOwner = this@CardFragment


        viewModel.observeEventCorrect(
            viewLifecycleOwner, preferences, dotsState, buzzer
        ) {
            showCorrectToast()
            updateTitle(title)
        }

        viewModel.observeEventIncorrect(
            viewLifecycleOwner, preferences, dotsState, buzzer
        ) {
            viewModel.symbol.value?.let { symbol ->
                require(symbol.length == 1)
                showIncorrectToast(dummyMaterialOf(symbol.first()))
            } ?: checkedToast(getString(R.string.input_loading))
            updateTitle(title)
        }

        viewModel.observeEventHint(
            viewLifecycleOwner, dotsState
        ) { expectedDots ->
            showHintDotsToast(expectedDots)
        }

        viewModel.observeEventPassHint(
            viewLifecycleOwner, dotsState
        ) {
            val symbol = viewModel.symbol.value ?: return@observeEventPassHint
            announceIntro(symbol)
        }

        viewModel.symbol.observe(
            viewLifecycleOwner,
            Observer {
                if (it == null) return@Observer
                announceIntro(it)
            }
        )

    }.root

    private fun announceIntro(symbol: String) {
        require(symbol.length == 1)
        val material = dummyMaterialOf(symbol.first())
        val intro = introStringNotNullLogged(material)
        announceByAccessibility(intro)
    }
}
