package com.github.braillesystems.learnbraille.data.repository

import com.github.braillesystems.learnbraille.data.entities.Step
import com.github.braillesystems.learnbraille.data.entities.StepDao


interface StepRepository {

    suspend fun getCurrentStep(userId: Long, courseId: Long): Step?
    suspend fun getCurrentStep(userId: Long, courseId: Long, lessonId: Long): Step?

    suspend fun getLastStep(userId: Long, courseId: Long): Step?
    suspend fun getLastStep(userId: Long, courseId: Long, lessonId: Long): Step?

    suspend fun getNextStep(userId: Long, courseId: Long, currentStepId: Long): Step?
    suspend fun getPrevStep(userId: Long, courseId: Long, currentStepId: Long): Step?
}

class StepRepositoryImpl(
    private val preferenceRepository: PreferenceRepository,
    private val stepDao: StepDao
) : StepRepository {

    override suspend fun getCurrentStep(userId: Long, courseId: Long): Step? {

        TODO("Not yet implemented")
    }

    override suspend fun getCurrentStep(userId: Long, courseId: Long, lessonId: Long): Step? {
        TODO("Not yet implemented")
    }

    override suspend fun getLastStep(userId: Long, courseId: Long): Step? {
        TODO("Not yet implemented")
    }

    override suspend fun getLastStep(userId: Long, courseId: Long, lessonId: Long): Step? {
        TODO("Not yet implemented")
    }

    override suspend fun getNextStep(userId: Long, courseId: Long, currentStepId: Long): Step? {
        TODO("Not yet implemented")
    }

    override suspend fun getPrevStep(userId: Long, courseId: Long, currentStepId: Long): Step? {
        TODO("Not yet implemented")
    }
}
