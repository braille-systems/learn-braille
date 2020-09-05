package com.github.braillesystems.learnbraille.data.repository

import com.github.braillesystems.learnbraille.data.entities.*

interface MaterialsRepository {
    suspend fun getRandomMaterialFromDeck(id: DBid): Material?
    suspend fun getAllMaterialsFromDeck(id: DBid): List<Material>
    suspend fun getAllDecks(): List<Deck>
}

open class MaterialsRepositoryImpl(
    private val deckDao: DeckDao,
    private val cardDao: CardDao
) : MaterialsRepository {

    override suspend fun getRandomMaterialFromDeck(id: DBid): Material? =
        cardDao.getRandomMaterialFromDeck(id)

    override suspend fun getAllMaterialsFromDeck(id: DBid): List<Material> =
        cardDao.getAllMaterialsFromDeck(id)

    override suspend fun getAllDecks(): List<Deck> = deckDao.getAllDecks()
}
