package com.github.braillesystems.learnbraille.ui.screens.lessons

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.ui.screens.HelpMsgId
import com.github.braillesystems.learnbraille.ui.screens.lessons.AbstractLesson.Companion.stepArgName
import com.github.braillesystems.learnbraille.ui.screens.menu.MenuFragment
import com.github.braillesystems.learnbraille.ui.screens.menu.MenuFragmentDirections
import com.github.braillesystems.learnbraille.utils.devnull
import com.github.braillesystems.learnbraille.utils.getStringArg
import com.github.braillesystems.learnbraille.utils.scope
import com.github.braillesystems.learnbraille.utils.side
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Base class for all lessons.
 */
abstract class AbstractLesson(helpMsgId: HelpMsgId) : AbstractFragmentWithHelp(helpMsgId) {

    override val helpMsg: String
        get() = super.helpMsg + getString(R.string.lessons_help_common)

    companion object {
        const val stepArgName = "step"
    }
}

fun AbstractLesson.getStepArg() =
    stepOf(
        getStringArg(stepArgName)
    )

private fun Fragment.navigateToStepHelper(
    nextStep: Step,
    userId: Long,
    lastStepDao: UserLastStepDao
): Unit =
    nextStep.toString().let { step ->
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
        Timber.i("Navigating to step with id = ${nextStep.id}")
        val lastStep =
            UserLastStep(
                userId,
                nextStep.id
            )
        scope().launch { lastStepDao.insertLastStep(lastStep) }
        findNavController().navigate(action)
    }

fun AbstractLesson.navigateToStep(nextStep: Step, userId: Long, lastStepDao: UserLastStepDao) =
    navigateToStepHelper(nextStep, userId, lastStepDao)

fun MenuFragment.navigateToStep(nextStep: Step, userId: Long, lastStepDao: UserLastStepDao) =
    navigateToStepHelper(nextStep, userId, lastStepDao)

fun AbstractLesson.navigateToPrevStep(
    current: Step,
    userId: Long,
    stepDao: StepDao,
    lastStepDao: UserLastStepDao
): Unit =
    if (current.data is FirstInfo) Timber.w("Trying to get step before first")
    else scope().launch {
        stepDao.getStep(current.id - 1)?.let { step ->
            navigateToStep(step, userId, lastStepDao)
        } ?: error("No step with less id")
    }.devnull

/**
 * Navigate to the next step if current is already passes.
 *
 * @param upsd Add current step to the UserPassedStep before navigation if not null.
 * @param onNextNotAvailable Is called when current step is not passed and `upsd` is `null`.
 */
fun AbstractLesson.navigateToNextStep(
    current: Step,
    userId: Long,
    stepDao: StepDao,
    lastStepDao: UserLastStepDao,
    upsd: UserPassedStepDao? = null,
    onNextNotAvailable: AbstractLesson.() -> Unit = {}
): Unit =
    if (current.data is LastInfo) Timber.w("Trying to get step after last")
    else scope().launch {
        upsd?.insertPassedStep(
            UserPassedStep(
                userId,
                current.id
            )
        )
        stepDao.getNextStepForUser(userId, current.id)?.let { step ->
            navigateToStep(step, userId, lastStepDao)
        } ?: Timber.i("On next step not available call").side {
            onNextNotAvailable()
        }
    }.devnull

/**
 * Navigate to the last passed step in course progress.
 */
fun AbstractLesson.navigateToCurrentStep(
    userId: Long,
    stepDao: StepDao,
    lastStepDao: UserLastStepDao
): Unit =
    scope().launch {
        navigateToStep(
            stepDao.getCurrentStepForUser(userId)
                ?: error("User ($userId) should always have at least one last step"),
            userId, lastStepDao
        )
    }.devnull

/**
 * Navigate to last step visited by user, or to the current step that always exists.
 */
fun MenuFragment.navigateToLastStep(
    userId: Long,
    stepDao: StepDao,
    lastStepDao: UserLastStepDao
): Unit =
    scope().launch {
        stepDao.getLastStepForUser(userId)?.let { step ->
            navigateToStep(step, userId, lastStepDao)
        } ?: navigateToStep(
            stepDao.getCurrentStepForUser(userId)
                ?: error("$userId does not have current step"),
            userId, lastStepDao
        )
    }.devnull
