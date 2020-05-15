package com.github.braillesystems.learnbraille.ui.brailletrainer

import android.app.Activity
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import com.github.braillesystems.learnbraille.utils.executeIf
import org.koin.core.KoinComponent
import org.koin.core.get

object BrailleTrainer : KoinComponent {

    fun init(activity: Activity): Unit = execute { UsbParser.init(activity) }
    fun trySend(brailleDots: BrailleDots): Unit = execute { UsbParser.trySend(brailleDots) }
    fun setSignalHandler(signalHandler: BrailleTrainerSignalHandler): Unit = execute {
        UsbParser.setSignalHandler(signalHandler)
    }

    /**
     * All this class public functionality must be call via this method.
     */
    private inline fun execute(block: () -> Unit) =
        executeIf(get<PreferenceRepository>().brailleTrainerEnabled, block)
}

interface BrailleTrainerSignalHandler : UsbSignalHandler
