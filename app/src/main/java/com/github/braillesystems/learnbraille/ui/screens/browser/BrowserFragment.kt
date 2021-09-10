package com.github.braillesystems.learnbraille.ui.screens.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.Deck
import com.github.braillesystems.learnbraille.data.entities.Material
import com.github.braillesystems.learnbraille.data.repository.MutableBrowserRepository
import com.github.braillesystems.learnbraille.databinding.BrowserListItemBinding
import com.github.braillesystems.learnbraille.databinding.FragmentBrowserBinding
import com.github.braillesystems.learnbraille.res.deckTagToName
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.utils.navigate
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class BrowserFragment : AbstractFragmentWithHelp(R.string.browser_help) {

    private val browserRepository: MutableBrowserRepository by inject()

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
            val decks = browserRepository.allDecksWithUniqueMaterials().map { DeckOrMaterial(it) }
            binding.materialsList.adapter = BrowserListAdapter(decks) { item ->
                this.item = item
                item.deck?.apply {
                    materialText.text = deckTagToName.getValue(tag)
                }
                clickListener = object : BrowserItemListener {
                    override fun onClick(item: DeckOrMaterial) {
                        item.deck?.apply {
                            // TODO Pass deck id to the BrowserDeckFragment via the fragment arguments
                            // TODO instead of the global persistent state.
                            browserRepository.currentDeckId = id
                            navigate(BrowserFragmentDirections.actionBrowserFragmentToBrowserDeckFragment())
                        }
                    }
                }
            }
        }

    }.root
}

class DeckOrMaterial {
    var material: Material? = null
    var deck: Deck? = null

    constructor(material: Material) {
        this.material = material
    }

    constructor(deck: Deck) {
        this.deck = deck
    }
}

class BrowserListAdapter<T>(
    private val items: List<T>,
    private val bind: BrowserListItemBinding.(T) -> Unit
) : RecyclerView.Adapter<BrowserItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BrowserItemViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.browser_list_item,
                parent, false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BrowserItemViewHolder, position: Int) {
        val item = items[position]
        holder.binding.bind(item)
    }
}

class BrowserItemViewHolder(
    val binding: BrowserListItemBinding
) : RecyclerView.ViewHolder(binding.root)

interface BrowserItemListener {
    fun onClick(item: DeckOrMaterial)
}
