package com.github.braillesystems.learnbraille.ui.screens.practice

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.repository.DeckWithAvailability
import com.github.braillesystems.learnbraille.data.repository.MutablePracticeRepository
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import com.github.braillesystems.learnbraille.databinding.DecksListItemBinding
import com.github.braillesystems.learnbraille.databinding.FragmentDecksListBinding
import com.github.braillesystems.learnbraille.res.deckTagToName
import com.github.braillesystems.learnbraille.utils.application
import com.github.braillesystems.learnbraille.utils.checkedToast
import com.github.braillesystems.learnbraille.utils.navigate
import com.github.braillesystems.learnbraille.utils.title
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class DecksListFragment : Fragment() {

    private val practiceRepository: MutablePracticeRepository by inject()
    private val preferenceRepository: PreferenceRepository by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentDecksListBinding>(
        inflater,
        R.layout.fragment_decks_list,
        container,
        false
    ).apply {

        title = getString(R.string.decks_list_title)

        lifecycleScope.launch {
            val decks = practiceRepository.allDecksWithAvailability()
            val currDeck = practiceRepository.currentDeck()
            val listener = object : DecksItemListener {
                override fun onClick(item: DeckWithAvailability) =
                    if (item.containsCards) {
                        practiceRepository.currentDeckId = item.deck.id
                        navigate(R.id.action_decksList_to_cardFragment)
                    } else {
                        checkedToast(
                            getString(R.string.decks_list_no_material_in_deck)
                                .format(deckTagToName.getValue(item.deck.tag))
                        )
                    }
            }
            decksList.adapter = DecksListAdapter(decks) { item ->
                this.item = item
                deckName.text = deckTagToName.getValue(item.deck.tag)
                deckName.setTypeface(
                    deckName.typeface,
                    if (item.deck == currDeck) Typeface.BOLD
                    else Typeface.NORMAL
                )
                clickListener = listener
                if (item.containsCards) {
                    deckName.setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.colorOnBackgroundDark
                        )
                    )
                    deckState.setImageResource(R.drawable.unlocked)
                } else {
                    deckName.setTextColor(
                        ContextCompat.getColor(
                            application,
                            R.color.colorOnBackgroundLight
                        )
                    )
                    deckState.setImageResource(R.drawable.locked)
                }
                if (!preferenceRepository.practiceUseOnlyKnownMaterials) {
                    deckState.visibility = View.GONE
                }
            }
        }

    }.root
}

private class DecksListAdapter(
    private val decks: List<DeckWithAvailability>,
    private val bind: DecksListItemBinding.(DeckWithAvailability) -> Unit
) : RecyclerView.Adapter<DeckItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DeckItemViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.decks_list_item,
                parent, false
            )
        )

    override fun getItemCount(): Int = decks.size

    override fun onBindViewHolder(holder: DeckItemViewHolder, position: Int) {
        val item = decks[position]
        holder.binding.bind(item)
    }
}

private class DeckItemViewHolder(
    val binding: DecksListItemBinding
) : RecyclerView.ViewHolder(binding.root)

interface DecksItemListener {
    fun onClick(item: DeckWithAvailability)
}
