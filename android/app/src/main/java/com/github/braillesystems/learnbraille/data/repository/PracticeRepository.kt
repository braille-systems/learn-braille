package com.github.braillesystems.learnbraille.data.repository

import android.content.Context
import com.github.braillesystems.learnbraille.data.dsl.ALL_CARDS_DECK_ID
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

    private val currDeckPreference: String
        get() = "practice_curr_deck_${preferenceRepository.currentUserId}"

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

    override suspend fun getNextMaterial(): Material =
        if (preferenceRepository.practiceUseOnlyKnownMaterials) {
            cardDao.getRandomKnownMaterialFromDeck(
                preferenceRepository.currentUserId, currentDeckId
            ) ?: run {
                currentDeckId = ALL_CARDS_DECK_ID
                getNextMaterial()
            }
        } else {
            cardDao
                .getRandomMaterialFromDeck(currentDeckId)
                ?: error(
                    "Material is expected to be prepopulated and " +
                            "user should not have possibilities to access empty deck"
                )
        }

    override suspend fun getCurrDeck(): Deck =
        deckDao.getDeck(currentDeckId) ?: error("Current deck should always exist")

    override suspend fun getAllDecks(): List<DeckNotEmpty> =
        if (preferenceRepository.practiceUseOnlyKnownMaterials) {
            val all = deckDao.getAllDecks()
            val available = deckDao.getAvailableDecks(preferenceRepository.currentUserId)
            all.map { DeckNotEmpty(it, available.contains(it)) }
        } else {
            deckDao.getAllDecks().map { DeckNotEmpty(it, true) }
        }
}
