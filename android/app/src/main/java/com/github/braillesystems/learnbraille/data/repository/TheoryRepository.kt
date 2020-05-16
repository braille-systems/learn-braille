package com.github.braillesystems.learnbraille.data.repository

import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.res.StepAnnotation

interface TheoryRepository {

    suspend fun getLastCourseStep(courseId: Long): Step
    suspend fun getAllCourseLessons(courseId: Long): List<Lesson>
}

interface MutableTheoryRepository : TheoryRepository {

    suspend fun getNextStepAndUpdate(thisStep: Step, markThisAsPassed: Boolean = false): Step?
    suspend fun getPrevStepAndUpdate(thisStep: Step): Step?
    suspend fun getCurrentStepAndUpdate(courseId: Long): Step
}

class TheoryRepositoryImpl(
    private val lessonDao: LessonDao,
    private val stepDao: StepDao,
    private val currentStepDao: CurrentStepDao,
    private val lastCourseStepDao: LastCourseStepDao,
    private val lastLessonStepDao: LastLessonStepDao,
    private val preferenceRepository: PreferenceRepository
) : MutableTheoryRepository {

    private val proscribedAnnotations
        get() = listOfNotNull(
            if (preferenceRepository.golubinaBookStepsEnabled) null
            else StepAnnotation.golubinaBookRequired
        )

    override suspend fun getNextStepAndUpdate(thisStep: Step, markThisAsPassed: Boolean): Step? {
        val next = stepDao.getNextStep(
            thisStep.courseId, thisStep.lessonId, thisStep.id,
            proscribedAnnotations
        ) ?: return null
        val curr = requireNotNull(
            stepDao.getCurrentStep(preferenceRepository.currentUserId, thisStep.courseId)
        )

        if (next <= curr) {
            updateLast(next)
            return next
        }
        if (!markThisAsPassed) return null

        currentStepDao.update(
            CurrentStep(
                preferenceRepository.currentUserId,
                next.courseId, next.lessonId, next.id
            )
        )
        updateLast(next)
        return next
    }

    override suspend fun getPrevStepAndUpdate(thisStep: Step): Step? =
        stepDao.getPrevStep(
            thisStep.courseId,
            thisStep.lessonId,
            thisStep.id,
            proscribedAnnotations
        )?.also { updateLast(it) }

    override suspend fun getCurrentStepAndUpdate(courseId: Long): Step =
        stepDao.getCurrentStep(preferenceRepository.currentUserId, courseId)
            ?.also { updateLast(it) }
            ?: initCoursePos(courseId)

    override suspend fun getLastCourseStep(courseId: Long): Step =
        stepDao.getLastStep(preferenceRepository.currentUserId, courseId)
            ?: initCoursePos(courseId)

    override suspend fun getAllCourseLessons(courseId: Long): List<Lesson> =
        lessonDao.getAllCourseLessons(courseId)

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
