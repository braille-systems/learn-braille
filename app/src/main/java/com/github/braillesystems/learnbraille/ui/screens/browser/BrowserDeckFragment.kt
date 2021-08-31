package com.github.braillesystems.learnbraille.ui.screens.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.MarkerSymbol
import com.github.braillesystems.learnbraille.data.entities.Material
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.data.entities.spelling
import com.github.braillesystems.learnbraille.data.repository.BrowserRepository
import com.github.braillesystems.learnbraille.databinding.FragmentBrowserBinding
import com.github.braillesystems.learnbraille.res.deckTagToName
import com.github.braillesystems.learnbraille.res.showMarkerPrintRules
import com.github.braillesystems.learnbraille.res.showSymbolPrintRules
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.utils.getValue
import com.github.braillesystems.learnbraille.utils.navigate
import com.github.braillesystems.learnbraille.utils.stringify
import com.github.braillesystems.learnbraille.utils.title
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class BrowserDeckFragment : AbstractFragmentWithHelp(R.string.browser_deck_help) {

    private val browserRepository: BrowserRepository by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentBrowserBinding>(
        inflater,
        R.layout.fragment_browser,
        container,
        false
    ).ini().also { binding ->
        lifecycleScope.launch {
            val deckId = browserRepository.currentDeckId
            browserRepository.deck(deckId)?.also { title = deckTagToName.getValue(it.tag) }
            val materials =
                browserRepository.allMaterialsFromDeck(deckId).map { DeckOrMaterial(it) }
            binding.materialsList.adapter = BrowserListAdapter(materials) { item ->
                this.item = item
                item.material?.apply {
                    materialText.text = when (data) {
                        is Symbol -> getString(R.string.browser_represent_template).format(
                            showSymbolPrintRules.getValue(data.char),
                            data.brailleDots.spelling
                        )
                        is MarkerSymbol -> getString(R.string.browser_represent_template).format(
                            showMarkerPrintRules.getValue(data.type),
                            data.brailleDots.spelling
                        )
                    }
                }
                clickListener = object : BrowserItemListener {
                    override fun onClick(item: DeckOrMaterial) {
                        item.material?.apply {
                            val arg = stringify(Material.serializer(), this)
                            when (data) {
                                is Symbol -> navigate(
                                    BrowserDeckFragmentDirections
                                        .actionBrowserDeckFragmentToSymbolViewFragment(arg)
                                )
                                is MarkerSymbol -> navigate(
                                    BrowserDeckFragmentDirections
                                        .actionBrowserDeckFragmentToMarkerViewFragment(arg)
                                )
                            }
                        }
                    }
                }
            }
        }

    }.root
}
