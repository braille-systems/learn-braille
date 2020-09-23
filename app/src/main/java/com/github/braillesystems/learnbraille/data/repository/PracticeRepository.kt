package com.github.braillesystems.learnbraille.data.repository

import android.content.Context
import com.github.braillesystems.learnbraille.data.dsl.ALL_CARDS_DECK_ID
import com.github.braillesystems.learnbraille.data.entities.DBid
import com.github.braillesystems.learnbraille.data.entities.Deck
import com.github.braillesystems.learnbraille.data.entities.DeckDao
import com.github.braillesystems.learnbraille.data.entities.Material
import com.github.braillesystems.learnbraille.utils.preferences

interface PracticeRepository : MaterialsRepository {
    val currentDeckId: DBid
    suspend fun currentDeck(): Deck
    suspend fun randomMaterial(): Material?
    suspend fun randomKnownMaterial(): Material?
}

interface MutablePracticeRepository : PracticeRepository {
    override var currentDeckId: DBid
}

class PracticeRepositoryImpl(
    private val context: Context,
    private val deckDao: DeckDao,
    private val preferenceRepository: PreferenceRepository,
    private val materialsRepository: MaterialsRepository
) : MutablePracticeRepository,
    MaterialsRepository by materialsRepository {

    private val currDeckPreference: String
        get() = "practice_curr_deck_${preferenceRepository.currentUserId}"

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

    override suspend fun currentDeck(): Deck =
        deckDao.deck(currentDeckId) ?: error("Wrong currentDeckId")

    override suspend fun randomMaterial(): Material? =
        materialsRepository.randomMaterialFromDeck(currentDeckId)

    override suspend fun randomKnownMaterial(): Material? =
        materialsRepository.randomKnownMaterialFromDeck(currentDeckId)
}
