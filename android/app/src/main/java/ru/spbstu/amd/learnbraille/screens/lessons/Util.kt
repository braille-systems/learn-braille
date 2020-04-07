package ru.spbstu.amd.learnbraille.screens.lessons

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.*
import ru.spbstu.amd.learnbraille.database.LearnBrailleDatabase
import ru.spbstu.amd.learnbraille.database.entities.*
import ru.spbstu.amd.learnbraille.defaultUser
import ru.spbstu.amd.learnbraille.screens.menu.MenuFragmentDirections

fun Fragment.navigateToNextStep(nextStep: Step): Unit =
    nextStep.toString()
        .let { step ->
            when (nextStep.data) {
                is Info -> MenuFragmentDirections.actionGlobalInfoFragment(step)
                is LastInfo -> MenuFragmentDirections.actionGlobalLastInfoFragment(step)
                is InputSymbol -> MenuFragmentDirections.actionGlobalInputSymbolFragment(step)
                is InputDots -> MenuFragmentDirections.actionGlobalInputDotsFragment(step)
                is ShowSymbol -> MenuFragmentDirections.actionGlobalShowSymbolFragment(step)
                is ShowDots -> MenuFragmentDirections.actionGlobalShowDotsFragment(step)
            }
        }
        .let { action ->
            findNavController().navigate(action)
        }

suspend fun getCurrentStep(database: StepDao, userId: Long): Step =
    withContext(Dispatchers.IO) {
        database.getCurrentStepForUser(userId)
            ?: error("User ($userId) should always have at least one last step")
    }

fun Fragment.navigateToNextStep() {
    // TODO refactor async
    val application: Application = requireNotNull(activity).application
    val database = LearnBrailleDatabase.getInstance(application).stepDao
    CoroutineScope(Dispatchers.Main + Job()).launch {
        val step = getCurrentStep(database, defaultUser)
        navigateToNextStep(step)
    }
}
