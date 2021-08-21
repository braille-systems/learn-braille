package com.github.braillesystems.learnbraille.ui.screens.theory

/**
 * Provides navigation to the specific fragment that depends on the step.
 */

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.data.repository.MutableTheoryRepository
import com.github.braillesystems.learnbraille.data.repository.TheoryRepository
import com.github.braillesystems.learnbraille.ui.screens.menu.MenuFragmentDirections
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.AbstractStepFragment
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.AbstractStepFragment.Companion.stepArgName
import com.github.braillesystems.learnbraille.utils.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import timber.log.Timber

fun getAction(step: Step): NavDirections =
    stringify(Step.serializer(), step).let { arg ->
        when (step.data) {
            is Info -> MenuFragmentDirections.actionGlobalInfoFragment(arg)
            is FirstInfo -> MenuFragmentDirections.actionGlobalFirstInfoFragment(arg)
            is LastInfo -> MenuFragmentDirections.actionGlobalLastInfoFragment(arg)
            is InputDots -> MenuFragmentDirections.actionGlobalInputDotsFragment(arg)
            is ShowDots -> MenuFragmentDirections.actionGlobalShowDotsFragment(arg)
            is Input -> when (step.data.material.data) {
                is Symbol -> MenuFragmentDirections.actionGlobalInputSymbolFragment(arg)
                is MarkerSymbol -> MenuFragmentDirections.actionGlobalInputMarkerFragment(arg)
            }
            is Show -> when (step.data.material.data) {
                is Symbol -> MenuFragmentDirections.actionGlobalShowSymbolFragment(arg)
                is MarkerSymbol -> MenuFragmentDirections.actionGlobalShowMarkerFragment(arg)
            }
        }
    }

fun AbstractStepFragment.getStepArg(): Step =
    parse(Step.serializer(), getFragmentStringArg(stepArgName))

fun Fragment.toStep(step: Step) = navigate(getAction(step))

fun AbstractStepFragment.toNextStep(
    thisStep: Step,
    theoryRepository: MutableTheoryRepository = get(),
    markThisAsPassed: Boolean,
    onNavigationFailed: Fragment.() -> Unit = {}
): Unit =
    if (thisStep.data is LastInfo) Timber.w("Tying to access step after last")
    else scope().launch {
        val nextStep = theoryRepository.nextStepAndMove(thisStep, markThisAsPassed)
        if (nextStep != null) {
            toStep(nextStep)
        } else {
            Timber.i("Next step navigation failed")
            onNavigationFailed()
        }
    }.devnull

fun AbstractStepFragment.toPrevStep(
    thisStep: Step,
    theoryRepository: MutableTheoryRepository = get()
): Unit =
    if (thisStep.data is FirstInfo) Timber.w("Trying to access step before first")
    else scope().launch {
        theoryRepository.prevStepAndMove(thisStep)
            ?.let(::toStep)
            ?: error("Prev step should always exist")
    }.devnull

fun AbstractStepFragment.toCurrentStep(
    courseId: Long,
    theoryRepository: MutableTheoryRepository = get()
): Unit = scope().launch {
    val currStep = theoryRepository.currentStepAndMove(courseId)
    toStep(currStep)
}.devnull

fun Fragment.toLastOrCurrCourseStep(
    courseId: Long,
    theoryRepository: TheoryRepository = get()
): Unit = scope().launch {
    val currStep = theoryRepository.currentStep(courseId)
    val lastStep = theoryRepository.lastCourseStep(courseId)
    toStep(
        // currStep < lastStep can occur after disabling teacher mode
        if (currStep < lastStep) currStep
        else lastStep
    )
}.devnull

fun Fragment.toLastLessonStep(
    courseId: Long, lessonId: Long, theoryRepository: MutableTheoryRepository = get()
): Unit = scope().launch {
    val lastStep = theoryRepository.lastLessonOrCurrentStepAndMove(courseId, lessonId)
    toStep(lastStep)
}.devnull
