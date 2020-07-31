package com.github.braillesystems.learnbraille.data.repository

import com.github.braillesystems.learnbraille.data.entities.CardDao
import com.github.braillesystems.learnbraille.data.entities.Deck
import com.github.braillesystems.learnbraille.data.entities.DeckDao
import com.github.braillesystems.learnbraille.data.entities.Material

interface MaterialsRepository {
    suspend fun getRandomMaterialFromDeck(id: Long): Material?
    suspend fun getAllMaterialsFromDeck(id: Long): List<Material>
    suspend fun getAllDecks(): List<Deck>
}

open class MaterialsRepositoryImpl(
    private val deckDao: DeckDao,
    private val cardDao: CardDao
) : MaterialsRepository {

    override suspend fun getRandomMaterialFromDeck(id: Long): Material? =
        cardDao.getRandomMaterialFromDeck(id)

    override suspend fun getAllMaterialsFromDeck(id: Long): List<Material> =
        cardDao.getAllMaterialsFromDeck(id)

    override suspend fun getAllDecks(): List<Deck> = deckDao.getAllDecks()
}
