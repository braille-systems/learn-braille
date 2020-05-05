package com.github.braillesystems.learnbraille.data.repository

import com.github.braillesystems.learnbraille.data.entities.CardDao
import com.github.braillesystems.learnbraille.data.entities.Material


interface CardRepository {

    suspend fun getNextMaterial(userId: Long): Material?
}

class CardRepositoryImpl(private val cardDao: CardDao) : CardRepository {

    override suspend fun getNextMaterial(userId: Long): Material? =
        cardDao.getNextMaterial()
}
