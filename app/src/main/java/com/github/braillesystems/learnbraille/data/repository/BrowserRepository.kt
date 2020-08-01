package com.github.braillesystems.learnbraille.data.repository

import android.content.Context
import com.github.braillesystems.learnbraille.data.dsl.ALL_CARDS_DECK_ID
import com.github.braillesystems.learnbraille.data.entities.CardDao
import com.github.braillesystems.learnbraille.data.entities.DeckDao
import com.github.braillesystems.learnbraille.utils.preferences

interface BrowserRepository : MaterialsRepository {
    val currentDeckId: Long
}

interface MutableBrowserRepository : BrowserRepository {
    override var currentDeckId: Long
}

class BrowserRepositoryImpl(
    private val context: Context,
    private val preferenceRepository: PreferenceRepository,
    deckDao: DeckDao,
    cardDao: CardDao
) : MaterialsRepositoryImpl(deckDao, cardDao),
    MutableBrowserRepository {

    private val currDeckPreference: String
        get() = "browser_curr_deck_${preferenceRepository.currentUserId}"

    override var currentDeckId: Long
        get() = context.preferences.getLong(
            currDeckPreference, ALL_CARDS_DECK_ID
        )
        set(value) {
            with(context.preferences.edit()) {
                putLong(currDeckPreference, value)
                apply()
            }
        }
}
