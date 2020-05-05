package com.github.braillesystems.learnbraille.ui.screens

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.data.repository.StepRepository
import com.github.braillesystems.learnbraille.ui.screens.menu.MenuFragmentDirections
import com.github.braillesystems.learnbraille.ui.screens.theory.AbstractStepFragment
import com.github.braillesystems.learnbraille.utils.*
import kotlinx.coroutines.launch
import timber.log.Timber


private const val stepArgsDelimiter = Char.MAX_VALUE


fun getAction(step: Step, courseId: Long): NavDirections =
    (stringify(Step.serializer(), step) + stepArgsDelimiter + courseId.toString()).let { args ->
        when (step.data) {
            is Info -> MenuFragmentDirections.actionGlobalInfoFragment(args)
            is FirstInfo -> MenuFragmentDirections.actionGlobalFirstInfoFragment(args)
            is LastInfo -> MenuFragmentDirections.actionGlobalLastInfoFragment(args)
            is InputDots -> MenuFragmentDirections.actionGlobalInputDotsFragment(args)
            is ShowDots -> MenuFragmentDirections.actionGlobalShowDotsFragment(args)
            is Input -> when (step.data.material.data) {
                is Symbol -> MenuFragmentDirections.actionGlobalInputSymbolFragment(args)
            }
            is Show -> when (step.data.material.data) {
                is Symbol -> MenuFragmentDirections.actionGlobalShowSymbolFragment(args)
            }
        }
    }

fun AbstractStepFragment.getStepAndCourseIdArgs(): Pair<Step, Long> =
    getStringArg(AbstractStepFragment.stepArgName).split(stepArgsDelimiter, limit = 2)
        .let { (stepString, courseIdString) ->
            parse(Step.serializer(), stepString) to courseIdString.toLong()
        }


fun Fragment.toStep(step: Step, courseId: Long) =
    findNavController().navigate(getAction(step, courseId))

fun Fragment.toNextStep(
    thisStep: Step,
    stepRepository: StepRepository,
    markThisAsPassed: Boolean,
    onNavigationFailed: Fragment.() -> Unit = {}
): Unit =
    if (thisStep.data is LastInfo) Timber.i("Last step reached")
    else scope().launch {
        stepRepository.getNextStep(thisStep.id, markThisAsPassed)
            ?.let { nextStep -> toStep(nextStep, stepRepository.thisCourseId) }
            ?: onNavigationFailed().also {
                Timber.i("Next step navigation failed")
            }
    }.devnull

fun Fragment.toPrevStep(
    thisStep: Step,
    stepRepository: StepRepository
): Unit =
    if (thisStep.data is FirstInfo) Timber.i("First step reached")
    else scope().launch {
        stepRepository.getPrevStep(thisStep.id)
            ?.let { nextStep -> toStep(nextStep, stepRepository.thisCourseId) }
            ?: error("Prev step should always exist")
    }.devnull

fun Fragment.toCurrentStep(
    stepRepository: StepRepository
): Unit = scope().launch {
    toStep(
        stepRepository.getCurrentStep(),
        stepRepository.thisCourseId
    )
}.devnull

fun Fragment.toLastStep(
    stepRepository: StepRepository
): Unit = scope().launch {
    toStep(
        stepRepository.getLastStep(),
        stepRepository.thisCourseId
    )
}.devnull
