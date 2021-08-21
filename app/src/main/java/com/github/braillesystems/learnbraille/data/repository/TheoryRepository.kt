package com.github.braillesystems.learnbraille.data.repository

import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.res.StepAnnotation
import com.github.braillesystems.learnbraille.utils.devnull
import com.github.braillesystems.learnbraille.utils.scope
import kotlinx.coroutines.launch

interface TheoryRepository {
    suspend fun currentStep(courseId: DBid): Step
    suspend fun lastCourseStep(courseId: DBid): Step
    suspend fun allCourseLessons(courseId: DBid): List<Lesson>
}

interface MutableTheoryRepository : TheoryRepository {
    suspend fun setCurrentStep(curr: CurrentStep)
    suspend fun nextStepAndMove(thisStep: Step, markThisAsPassed: Boolean = false): Step?
    suspend fun prevStepAndMove(thisStep: Step): Step?
    suspend fun currentStepAndMove(courseId: DBid): Step
    suspend fun lastLessonOrCurrentStepAndMove(courseId: DBid, lessonId: DBid): Step
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

    private val proscribedAnnotations: List<String>
        get() = listOfNotNull(
            if (preferenceRepository.golubinaBookStepsEnabled) null
            else StepAnnotation.golubinaBookRequired,
            if (preferenceRepository.slateStylusStepsEnabled) null
            else StepAnnotation.slateStylusRequired
        )

    override suspend fun setCurrentStep(curr: CurrentStep) =
        currentStepDao.update(curr)

    @Suppress("ReturnCount")
    override suspend fun nextStepAndMove(thisStep: Step, markThisAsPassed: Boolean): Step? {
        val next = stepDao
            .nextStep(
                thisStep.courseId, thisStep.lessonId, thisStep.id,
                proscribedAnnotations
            )
            ?: return null
        val curr = stepDao
            .currentStep(preferenceRepository.currentUserId, thisStep.courseId)!!

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

    override suspend fun prevStepAndMove(thisStep: Step): Step? =
        stepDao
            .prevStep(
                thisStep.courseId,
                thisStep.lessonId,
                thisStep.id,
                proscribedAnnotations
            )
            ?.also { updateLast(it) }

    override suspend fun currentStepAndMove(courseId: DBid): Step =
        currentStep(courseId).also { updateLast(it) }

    /**
     * LessonId is supposed to exist for this courseId.
     */
    override suspend fun lastLessonOrCurrentStepAndMove(courseId: DBid, lessonId: DBid): Step {
        val lastStep = stepDao.lastStep(preferenceRepository.currentUserId, courseId, lessonId)
        if (lastStep != null) {
            updateLast(lastStep)
            return lastStep
        }
        if (preferenceRepository.teacherModeEnabled) {
            val step = stepDao.step(courseId, lessonId)
                ?: error("No such lessonId ($lessonId) exists for course ($courseId)")
            updateLast(step)
            return step
        }
        error(
            "No such lessonId ($lessonId) exists for course ($courseId) " +
                    "or current step is behind lesson with such lessonId"
        )
    }

    override suspend fun currentStep(courseId: DBid): Step =
        stepDao
            .currentStep(preferenceRepository.currentUserId, courseId)
            ?: initCoursePos(courseId)

    override suspend fun lastCourseStep(courseId: DBid): Step =
        stepDao
            .lastStep(preferenceRepository.currentUserId, courseId)
            ?: initCoursePos(courseId)

    override suspend fun allCourseLessons(courseId: DBid): List<Lesson> =
        lessonDao.allCourseLessons(courseId)

    private suspend fun initCoursePos(courseId: DBid): Step =
        stepDao
            .firstCourseStep(courseId)
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
        if (step.data is Input) {
            knownMaterialDao.insert(
                KnownMaterial(
                    preferenceRepository.currentUserId,
                    step.data.material.id
                )
            )
        }
    }.devnull
}
