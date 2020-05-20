package com.github.braillesystems.learnbraille.data.repository

import android.content.Context
import com.github.braillesystems.learnbraille.data.entities.CardDao
import com.github.braillesystems.learnbraille.data.entities.Deck
import com.github.braillesystems.learnbraille.data.entities.DeckDao
import com.github.braillesystems.learnbraille.data.entities.Material
import com.github.braillesystems.learnbraille.utils.preferences

data class DeckNotEmpty(
    val deck: Deck,
    val containsCards: Boolean
)

interface PracticeRepository {

    val currentDeckId: Long

    suspend fun getNextMaterial(): Material
    suspend fun getCurrDeck(): Deck
    suspend fun getAllDecks(): List<DeckNotEmpty>
}

interface MutablePracticeRepository : PracticeRepository {

    override var currentDeckId: Long
}

class PracticeRepositoryImpl(
    private val context: Context,
    private val deckDao: DeckDao,
    private val cardDao: CardDao,
    private val preferenceRepository: PreferenceRepository
) : MutablePracticeRepository {

    private val currDeckPreference get() = "practice_curr_deck ${preferenceRepository.currentUserId}"

    override var currentDeckId: Long
        get() = context.preferences.getLong(
            currDeckPreference, 1
        )
        set(value) {
            with(context.preferences.edit()) {
                putLong(currDeckPreference, value)
                apply()
            }
        }

    // TODO deck filling constraints
    override suspend fun getNextMaterial(): Material =
        cardDao.getRandomMaterialFromDeck(currentDeckId)
            ?: error("Material is expected to be prepopulated")

    override suspend fun getCurrDeck(): Deck =
        deckDao.getDeck(currentDeckId) ?: error("Current deck should always exist")

    override suspend fun getAllDecks(): List<DeckNotEmpty> =
        deckDao.getAllDecks().map { DeckNotEmpty(it, true) }
}
