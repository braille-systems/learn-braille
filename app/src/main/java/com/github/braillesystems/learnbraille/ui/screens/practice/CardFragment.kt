package com.github.braillesystems.learnbraille.ui.screens.practice

import android.os.Bundle
import android.os.Vibrator
import android.util.TypedValue
import android.view.*
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.MarkerSymbol
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import com.github.braillesystems.learnbraille.databinding.FragmentCardBinding
import com.github.braillesystems.learnbraille.res.captionRules
import com.github.braillesystems.learnbraille.res.deckTagToName
import com.github.braillesystems.learnbraille.res.inputMarkerPrintRules
import com.github.braillesystems.learnbraille.ui.*
import com.github.braillesystems.learnbraille.ui.brailletrainer.BrailleTrainer
import com.github.braillesystems.learnbraille.ui.brailletrainer.BrailleTrainerSignalHandler
import com.github.braillesystems.learnbraille.ui.screens.*
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsState
import com.github.braillesystems.learnbraille.ui.views.brailleDots
import com.github.braillesystems.learnbraille.ui.views.dotsState
import com.github.braillesystems.learnbraille.ui.views.subscribe
import com.github.braillesystems.learnbraille.utils.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class CardFragment : AbstractFragmentWithHelp(R.string.practice_help) {

    private val preferenceRepository: PreferenceRepository by inject()

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
    ) = DataBindingUtil.inflate<FragmentCardBinding>(
        inflater,
        R.layout.fragment_card,
        container,
        false
    ).also { binding ->

        Timber.i("onCreateView")

        updateTitle(title)
        setHasOptionsMenu(true)

        if (preferenceRepository.extendedAccessibilityEnabled) {
            binding.hintButton.setSize(
                width = resources.getDimension(R.dimen.side_buttons_extended_width).toInt()
            )
            binding.nextButton.setSize(
                width = resources.getDimension(R.dimen.side_buttons_extended_width).toInt()
            )
            binding.markerDescription.setTextSize(
                TypedValue.COMPLEX_UNIT_SP,
                application.extendedTextSize
            )
        }

        dotsState = binding.brailleDots.dotsState.apply {
            subscribe(View.OnClickListener {
                viewModel.onSoftCheck()
            })
        }

        val viewModelFactory: CardViewModelFactory by inject {
            parametersOf({ dotsState.brailleDots })
        }
        viewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(CardViewModel::class.java)
        buzzer = activity?.getSystemService()
        BrailleTrainer.setSignalHandler(object : BrailleTrainerSignalHandler {
            override fun onJoystickRight() = viewModel.onCheck()
            override fun onJoystickLeft() = viewModel.onHint()
        })


        binding.cardViewModel = viewModel
        binding.lifecycleOwner = this@CardFragment


        viewModel.symbol.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            when (it) {
                is Symbol -> {
                    binding.letter.visibility = View.VISIBLE
                    binding.markerDescription.visibility = View.GONE
                    binding.letter.text = it.char.toString()
                    binding.letterCaption.text = captionRules.getValue(it)
                }
                is MarkerSymbol -> {
                    binding.letter.visibility = View.GONE
                    binding.markerDescription.visibility = View.VISIBLE
                    binding.markerDescription.text = contextNotNull.inputMarkerPrintRules[it.type]
                    binding.letterCaption.text = ""
                }
            }
        })

        viewModel.observeCheckedOnFly(
            viewLifecycleOwner, dotsState, buzzer,
            block = { updateTitle(title) },
            softBlock = ::showCorrectToast
        )

        viewModel.observeEventIncorrect(
            viewLifecycleOwner, dotsState, buzzer
        ) {
            viewModel.symbol.value?.let { symbol ->
                when (symbol) {
                    is Symbol -> showIncorrectToast(symbol.char)
                    is MarkerSymbol -> showIncorrectToast()
                }
            } ?: checkedToast(getString(R.string.input_loading))
            updateTitle(title)
        }

        viewModel.observeEventHint(
            viewLifecycleOwner, dotsState
        ) { expectedDots ->
            showHintToast(expectedDots)
        }

        viewModel.observeEventPassHint(
            viewLifecycleOwner, dotsState
        ) {
            viewModel.symbol.value?.let {
                when (it) {
                    is Symbol -> announceIntro(it.char.toString())
                    is MarkerSymbol -> checkedAnnounce(contextNotNull.inputMarkerPrintRules[it.type]!!)
                }
            }
        }

        viewModel.symbol.observe(
            viewLifecycleOwner,
            Observer {
                if (it == null) return@Observer
                when (it) {
                    is Symbol -> announceIntro(it.char.toString())
                    is MarkerSymbol -> checkedAnnounce(contextNotNull.inputMarkerPrintRules[it.type]!!)
                }
            }
        )

        viewModel.deckTag.observe(
            viewLifecycleOwner,
            Observer { tag ->
                if (tag == null) return@Observer
                val template = if (preferenceRepository.practiceUseOnlyKnownMaterials) {
                    getString(R.string.practice_deck_name_enabled_template)
                } else {
                    getString(R.string.practice_deck_name_disabled_template)
                }
                toast(template.format(deckTagToName.getValue(tag)))
            }
        )

    }.root

    private fun announceIntro(symbol: String) {
        require(symbol.length == 1)
        val intro = printStringNotNullLogged(symbol.first(), PrintMode.INPUT)
        checkedAnnounce(intro)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.card_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = false.also {
        when (item.itemId) {
            R.id.help -> navigateToHelp()
            R.id.decks_list -> navigate(R.id.action_cardFragment_to_decksList)
        }
    }
}
