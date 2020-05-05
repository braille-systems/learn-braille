package com.github.braillesystems.learnbraille.data.repository

import com.github.braillesystems.learnbraille.data.entities.*


interface StepRepository {

    val thisCourseId: Long

    suspend fun getCurrentStep(): Step
    suspend fun getLastStep(): Step
    suspend fun getNextStep(thisStepId: Long): Step?
    suspend fun getPrevStep(thisStepId: Long): Step?

    suspend fun updateLastStep(thisStepId: Long)
    suspend fun updateCurrentStep(thisStepId: Long)
}

class StepRepositoryImpl(
    override val thisCourseId: Long,
    private val preferenceRepository: PreferenceRepository,
    private val stepDao: StepDao,
    private val currentStepDao: CurrentStepDao,
    private val lastCourseStepDao: LastCourseStepDao
) : StepRepository {

    override suspend fun getCurrentStep(): Step {
        var currentStep = stepDao.getCurrentStep(preferenceRepository.currentUserId, thisCourseId)
        if (currentStep != null) return currentStep

        currentStep = stepDao.getFirstCourseStep(thisCourseId)
            ?: error("Course id should be correct, and course should not be empty")
        currentStepDao.insert(
            CurrentStep(
                preferenceRepository.currentUserId,
                thisCourseId, currentStep.id
            )
        )
        return currentStep
    }

    override suspend fun getLastStep(): Step {
        var lastStep = stepDao.getLastStep(preferenceRepository.currentUserId, thisCourseId)
        if (lastStep != null) return lastStep

        lastStep = stepDao.getFirstCourseStep(thisCourseId)
            ?: error("Course id should be correct, and course should not be empty")
        lastCourseStepDao.insert(
            LastCourseStep(
                preferenceRepository.currentUserId,
                thisCourseId, lastStep.id
            )
        )
        return lastStep
    }

    override suspend fun getNextStep(thisStepId: Long): Step? =
        stepDao.getNextStep(preferenceRepository.currentUserId, thisCourseId, thisStepId)

    override suspend fun getPrevStep(thisStepId: Long): Step? =
        stepDao.getPrevStep(thisCourseId, thisStepId)

    override suspend fun updateLastStep(thisStepId: Long) {
        lastCourseStepDao.update(
            LastCourseStep(
                preferenceRepository.currentUserId,
                thisCourseId, thisStepId
            )
        )
    }

    /**
     * @param thisStepId Id of passed step.
     */
    override suspend fun updateCurrentStep(thisStepId: Long) {
        val currentStep = getCurrentStep()
        val newCurrentStepId = thisStepId + 1
        if (newCurrentStepId > currentStep.id) {
            currentStepDao.update(
                CurrentStep(
                    preferenceRepository.currentUserId,
                    thisCourseId, newCurrentStepId
                )
            )
        }
    }
}
