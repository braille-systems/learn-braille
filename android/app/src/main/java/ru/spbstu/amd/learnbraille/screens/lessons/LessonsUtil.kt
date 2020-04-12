package ru.spbstu.amd.learnbraille.screens.lessons

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.spbstu.amd.learnbraille.coroutineContext
import ru.spbstu.amd.learnbraille.database.entities.*
import ru.spbstu.amd.learnbraille.screens.menu.MenuFragmentDirections
import ru.spbstu.amd.learnbraille.side
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

fun AbstractLesson.navigateToPrevStep(dataSource: StepDao, current: Step) {
    if (current.id == 1L) return
    CoroutineScope(Dispatchers.Main + Job()).launch {
        val step = coroutineContext {
            dataSource.getStep(current.id - 1) ?: error("No step with less id")
        }
        navigateToStep(step)
    }
}

fun AbstractLesson.navigateToNextStep(
    dataSource: StepDao,
    current: Step,
    userId: Long,
    upsd: UserPassedStepDao? = null,
    onNextNotAvailable: AbstractLesson.() -> Unit = {}
) {
    if (current.data is LastInfo) {
        Timber.w("Trying to get step after last")
        return
    }
    CoroutineScope(Dispatchers.Main + Job()).launch {
        upsd?.let {
            addPassedStep(it, UserPassedStep(userId, current.id))
        }
        coroutineContext {
            dataSource.getNextStepForUser(userId, current.id)
        }?.let { step ->
            navigateToStep(step)
        } ?: onNextNotAvailable().side {
            Timber.i("On next step not available call")
        }
    }
}

fun Fragment.navigateToCurrentStep(
    dataSource: StepDao,
    userId: Long
) {
    CoroutineScope(Dispatchers.Main + Job()).launch {
        val step = coroutineContext {
            getCurrentStep(dataSource, userId)
        }
        navigateToStep(step)
    }
}

suspend fun addPassedStep(dataSource: UserPassedStepDao, ups: UserPassedStep): Unit =
    coroutineContext {
        dataSource.insertPassedStep(ups)
    }

suspend fun getCurrentStep(database: StepDao, userId: Long): Step =
    coroutineContext {
        database.getCurrentStepForUser(userId)
            ?: error("User ($userId) should always have at least one last step")
    }
