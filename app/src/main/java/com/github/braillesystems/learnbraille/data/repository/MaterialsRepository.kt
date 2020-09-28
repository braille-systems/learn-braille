package com.github.braillesystems.learnbraille.data.repository

import com.github.braillesystems.learnbraille.data.entities.*

data class DeckWithAvailability(
    val deck: Deck,
    val containsCards: Boolean
)

interface MaterialsRepository {
    suspend fun randomMaterialFromDeck(id: DBid): Material?
    suspend fun randomKnownMaterialFromDeck(id: DBid): Material?
    suspend fun allMaterialsFromDeck(id: DBid): List<Material>
    suspend fun allDecks(): List<Deck>
    suspend fun availableDecks(): List<Deck>
    suspend fun allDecksWithAvailability(): List<DeckWithAvailability>
}

open class MaterialsRepositoryImpl(
    private val deckDao: DeckDao,
    private val cardDao: CardDao,
    private val preferenceRepository: PreferenceRepository
) : MaterialsRepository {

    override suspend fun randomMaterialFromDeck(id: DBid): Material? =
        cardDao.randomMaterialFromDeck(id)

    override suspend fun randomKnownMaterialFromDeck(id: DBid): Material? =
        cardDao.randomKnownMaterialFromDeck(preferenceRepository.currentUserId, id)

    override suspend fun allMaterialsFromDeck(id: DBid): List<Material> =
        cardDao.allMaterialsFromDeck(id)

    override suspend fun allDecks(): List<Deck> = deckDao.allDecks()

    override suspend fun availableDecks(): List<Deck> =
        deckDao.availableDecks(preferenceRepository.currentUserId)

    override suspend fun allDecksWithAvailability(): List<DeckWithAvailability> =
        if (preferenceRepository.practiceUseOnlyKnownMaterials) {
            val all = allDecks()
            val available = availableDecks()
            all.map { DeckWithAvailability(it, available.contains(it)) }
        } else {
            deckDao.allDecks().map { DeckWithAvailability(it, true) }
        }
}
