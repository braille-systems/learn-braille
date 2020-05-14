package com.github.braillesystems.learnbraille.data.repository

import com.github.braillesystems.learnbraille.data.entities.*
import timber.log.Timber

interface TheoryRepository {

    suspend fun getLastCourseStep(courseId: Long): Step
}

interface MutableTheoryRepository : TheoryRepository {

    suspend fun getNextStepAndUpdate(thisStep: Step, markThisAsPassed: Boolean = false): Step?
    suspend fun getPrevStepAndUpdate(thisStep: Step): Step?
    suspend fun getCurrentStepAndUpdate(courseId: Long): Step
}

class TheoryRepositoryImpl(
    private val stepDao: StepDao,
    private val currentStepDao: CurrentStepDao,
    private val lastCourseStepDao: LastCourseStepDao,
    private val lastLessonStepDao: LastLessonStepDao,
    private val preferenceRepository: PreferenceRepository
) : MutableTheoryRepository {

    override suspend fun getNextStepAndUpdate(thisStep: Step, markThisAsPassed: Boolean): Step? {
        if (markThisAsPassed) tryUpdateCurrentStep(thisStep)
        val next = stepDao.getNextStep(
            preferenceRepository.currentUserId,
            thisStep.courseId, thisStep.lessonId, thisStep.id
        )
        if (next != null) updateLast(next)
        return next
    }

    override suspend fun getPrevStepAndUpdate(thisStep: Step): Step? =
        stepDao.getPrevStep(thisStep.courseId, thisStep.lessonId, thisStep.id)
            ?.also { updateLast(it) }

    override suspend fun getCurrentStepAndUpdate(courseId: Long): Step =
        stepDao.getCurrentStep(preferenceRepository.currentUserId, courseId)
            ?.also { updateLast(it) }
            ?: initCoursePos(courseId)

    override suspend fun getLastCourseStep(courseId: Long): Step =
        stepDao.getLastStep(preferenceRepository.currentUserId, courseId)
            ?: initCoursePos(courseId)

    private suspend fun initCoursePos(courseId: Long): Step =
        stepDao.getFirstCourseStep(courseId)?.also { first ->
            updateLast(first)
            currentStepDao.update(
                CurrentStep(
                    preferenceRepository.currentUserId,
                    first.courseId, first.lessonId, first.id
                )
            )
        } ?: error("First course step should always exist")

    private suspend fun tryUpdateCurrentStep(thisStep: Step) {
        val curr = stepDao.getCurrentStep(
            preferenceRepository.currentUserId,
            thisStep.courseId
        )
        requireNotNull(curr)
        if (curr != thisStep) return

        val last = stepDao.getLastLessonStep(thisStep.courseId, thisStep.lessonId)
            ?: error("Course with this lesson does not exist")
        val newCurr = if (last.id == thisStep.id) {
            thisStep.copy(id = 1, lessonId = thisStep.lessonId + 1)
        } else {
            thisStep.copy(id = thisStep.id + 1)
        }
        Timber.i("Update curr step from $curr to $newCurr")
        currentStepDao.update(
            CurrentStep(
                preferenceRepository.currentUserId,
                newCurr.courseId, newCurr.lessonId, newCurr.id
            )
        )
    }

    private suspend fun updateLast(step: Step) {
        lastCourseStepDao.update(
            LastCourseStep(
                preferenceRepository.currentUserId,
                step.courseId, step.lessonId, step.id
            )
        )
        lastLessonStepDao.update(
            LastLessonStep(
                preferenceRepository.currentUserId,
                step.courseId, step.lessonId, step.id
            )
        )
    }
}
