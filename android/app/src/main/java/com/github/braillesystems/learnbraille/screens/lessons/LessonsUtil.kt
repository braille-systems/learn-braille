package com.github.braillesystems.learnbraille.screens.lessons

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.braillesystems.learnbraille.database.entities.*
import com.github.braillesystems.learnbraille.screens.menu.MenuFragmentDirections
import com.github.braillesystems.learnbraille.util.devnull
import com.github.braillesystems.learnbraille.util.scope
import com.github.braillesystems.learnbraille.util.side
import kotlinx.coroutines.launch
import timber.log.Timber

fun Fragment.navigateToStep(nextStep: Step): Unit =
    nextStep.toString().also {
        Timber.i("Navigating to step with id = ${nextStep.id}")
    }.let { step ->
        when (nextStep.data) {
            is Info -> MenuFragmentDirections.actionGlobalInfoFragment(step)
            is FirstInfo -> MenuFragmentDirections.actionGlobalFirstInfoFragment(step)
            is LastInfo -> MenuFragmentDirections.actionGlobalLastInfoFragment(step)
            is InputSymbol -> MenuFragmentDirections.actionGlobalInputSymbolFragment(step)
            is InputDots -> MenuFragmentDirections.actionGlobalInputDotsFragment(step)
            is ShowSymbol -> MenuFragmentDirections.actionGlobalShowSymbolFragment(step)
            is ShowDots -> MenuFragmentDirections.actionGlobalShowDotsFragment(step)
        }
    }.side { action ->
        findNavController().navigate(action)
    }

fun AbstractLesson.navigateToPrevStep(dataSource: StepDao, current: Step): Unit =
    if (current.data is FirstInfo) Timber.w("Trying to get step before first")
    else scope().launch {
        dataSource.getStep(current.id - 1)?.let { step ->
            navigateToStep(step)
        } ?: error("No step with less id")
    }.devnull

fun AbstractLesson.navigateToNextStep(
    dataSource: StepDao,
    current: Step,
    userId: Long,
    upsd: UserPassedStepDao? = null,
    onNextNotAvailable: AbstractLesson.() -> Unit = {}
): Unit =
    if (current.data is LastInfo) Timber.w("Trying to get step after last")
    else scope().launch {
        upsd?.insertPassedStep(UserPassedStep(userId, current.id))
        dataSource.getNextStepForUser(userId, current.id)?.let { step ->
            navigateToStep(step)
        } ?: onNextNotAvailable().side {
            Timber.i("On next step not available call")
        }
    }.devnull

fun Fragment.navigateToCurrentStep(
    dataSource: StepDao,
    userId: Long
): Unit = scope().launch {
    navigateToStep(
        dataSource.getCurrentStepForUser(userId)
            ?: error("User ($userId) should always have at least one last step")
    )
}.devnull
