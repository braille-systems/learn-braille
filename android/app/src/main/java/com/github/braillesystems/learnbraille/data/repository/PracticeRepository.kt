package com.github.braillesystems.learnbraille.data.repository

import com.github.braillesystems.learnbraille.data.entities.Material
import com.github.braillesystems.learnbraille.data.entities.MaterialDao

interface PracticeRepository {

    suspend fun getNextMaterial(): Material
}

interface MutablePracticeRepository : PracticeRepository

class PracticeRepositoryImpl(
    private val materialDao: MaterialDao
) : MutablePracticeRepository {

    override suspend fun getNextMaterial(): Material =
        materialDao.getRandomMaterial()
            ?: error("Material is expected to be prepopulated")
}
