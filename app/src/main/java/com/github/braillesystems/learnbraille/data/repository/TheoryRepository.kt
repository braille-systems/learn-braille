package com.github.braillesystems.learnbraille.data.repository

import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.res.StepAnnotation
import com.github.braillesystems.learnbraille.utils.devnull
import com.github.braillesystems.learnbraille.utils.scope
import kotlinx.coroutines.launch

interface TheoryRepository {
    suspend fun getCurrentStep(courseId: Long): Step
    suspend fun getLastCourseStep(courseId: Long): Step
    suspend fun getAllCourseLessons(courseId: Long): List<Lesson>
}

interface MutableTheoryRepository : TheoryRepository {
    suspend fun getNextStepAndUpdate(thisStep: Step, markThisAsPassed: Boolean = false): Step?
    suspend fun getPrevStepAndUpdate(thisStep: Step): Step?
    suspend fun getCurrentStepAndUpdate(courseId: Long): Step
    suspend fun getLastLessonOrCurrentStepAndUpdate(courseId: Long, lessonId: Long): Step
}

class TheoryRepositoryImpl(
    private val lessonDao: LessonDao,
    private val stepDao: StepDao,
    private val currentStepDao: CurrentStepDao,
    private val lastCourseStepDao: LastCourseStepDao,
    private val lastLessonStepDao: LastLessonStepDao,
    private val knownMaterialDao: KnownMaterialDao,
    private val preferenceRepository: PreferenceRepository,
    private val actionsRepository: MutableActionsRepository
) : MutableTheoryRepository {

    private val proscribedAnnotations
        get() = listOfNotNull(
            if (preferenceRepository.golubinaBookStepsEnabled) null
            else StepAnnotation.golubinaBookRequired
        )

    override suspend fun getNextStepAndUpdate(thisStep: Step, markThisAsPassed: Boolean): Step? {
        val next = stepDao
            .getNextStep(
                thisStep.courseId, thisStep.lessonId, thisStep.id,
                proscribedAnnotations
            )
            ?: return null
        val curr = requireNotNull(
            stepDao.getCurrentStep(preferenceRepository.currentUserId, thisStep.courseId)
        )

        val updateAndReturn = {
            updateLast(next)
            updateKnown(thisStep)
            next
        }

        if (next <= curr) {
            return updateAndReturn()
        }
        if (!markThisAsPassed) return null

        currentStepDao.update(
            CurrentStep(
                preferenceRepository.currentUserId,
                next.courseId, next.lessonId, next.id
            )
        )
        actionsRepository.addAction(TheoryPassStep(thisStep.data is BaseInput))

        return updateAndReturn()
    }

    override suspend fun getPrevStepAndUpdate(thisStep: Step): Step? =
        stepDao
            .getPrevStep(
                thisStep.courseId,
                thisStep.lessonId,
                thisStep.id,
                proscribedAnnotations
            )
            ?.also { updateLast(it) }

    override suspend fun getCurrentStepAndUpdate(courseId: Long): Step =
        getCurrentStep(courseId).also { updateLast(it) }

    /**
     * LessonId is supposed to exist for this courseId.
     */
    override suspend fun getLastLessonOrCurrentStepAndUpdate(courseId: Long, lessonId: Long): Step =
        stepDao
            .getLastStep(preferenceRepository.currentUserId, courseId, lessonId)
            ?.also { updateLast(it) }
            ?: error(
                "No such lessonId ($lessonId) exists for course ($courseId) " +
                        "or current step is behind lesson with such lessonId"
            )

    override suspend fun getCurrentStep(courseId: Long): Step =
        stepDao
            .getCurrentStep(preferenceRepository.currentUserId, courseId)
            ?: initCoursePos(courseId)

    override suspend fun getLastCourseStep(courseId: Long): Step =
        stepDao
            .getLastStep(preferenceRepository.currentUserId, courseId)
            ?: initCoursePos(courseId)

    override suspend fun getAllCourseLessons(courseId: Long): List<Lesson> =
        lessonDao.getAllCourseLessons(courseId)

    private suspend fun initCoursePos(courseId: Long): Step =
        stepDao
            .getFirstCourseStep(courseId)
            ?.also { first ->
                updateLast(first)
                currentStepDao.update(
                    CurrentStep(
                        preferenceRepository.currentUserId,
                        first.courseId, first.lessonId, first.id
                    )
                )
            }
            ?: error("First course step should always exist")

    private fun updateLast(step: Step): Unit = scope().launch {
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
    }.devnull

    private fun updateKnown(step: Step): Unit = scope().launch {
        when (step.data) {
            is Input -> knownMaterialDao.insert(
                KnownMaterial(
                    preferenceRepository.currentUserId,
                    step.data.material.id
                )
            )
        }
    }.devnull
}
