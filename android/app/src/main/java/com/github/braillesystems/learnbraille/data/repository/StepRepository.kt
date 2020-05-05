package com.github.braillesystems.learnbraille.data.repository

import com.github.braillesystems.learnbraille.data.entities.Step
import com.github.braillesystems.learnbraille.data.entities.StepDao


interface StepRepository {

    val thisCourseId: Long

    suspend fun getCurrentStep(): Step
    suspend fun getLastStep(): Step
    suspend fun getNextStep(thisStepId: Long, markThisAsPassed: Boolean): Step?
    suspend fun getPrevStep(thisStepId: Long): Step?
}

class StepRepositoryImpl(
    override val thisCourseId: Long,
    private val preferenceRepository: PreferenceRepository,
    private val stepDao: StepDao
) : StepRepository {

    override suspend fun getCurrentStep(): Step {
        TODO("Not yet implemented")
    }

    override suspend fun getLastStep(): Step {
        TODO("Not yet implemented")
    }

    override suspend fun getNextStep(thisStepId: Long, markThisAsPassed: Boolean): Step? {
        TODO("Not yet implemented")
    }

    override suspend fun getPrevStep(thisStepId: Long): Step? {
        TODO("Not yet implemented")
    }
}
