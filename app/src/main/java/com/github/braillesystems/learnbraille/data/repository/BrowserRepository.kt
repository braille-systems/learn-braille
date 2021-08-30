package com.github.braillesystems.learnbraille.data.repository

import android.content.Context
import com.github.braillesystems.learnbraille.data.dsl.ALL_CARDS_DECK_ID
import com.github.braillesystems.learnbraille.data.entities.DBid
import com.github.braillesystems.learnbraille.utils.preferences

interface BrowserRepository : MaterialsRepository {
    var currentDeckId: DBid
}

interface MutableBrowserRepository : BrowserRepository {
    override var currentDeckId: DBid
}

class BrowserRepositoryImpl(
    private val context: Context,
    private val preferenceRepository: PreferenceRepository,
    private val materialsRepository: MaterialsRepository
) : MutableBrowserRepository,
    MaterialsRepository by materialsRepository {

    private val currDeckPreference: String
        get() = "browser_curr_deck_${preferenceRepository.currentUserId}"

    override var currentDeckId: DBid
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
