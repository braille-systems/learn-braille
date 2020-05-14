package com.github.braillesystems.learnbraille.ui.brailletrainer

import android.app.Activity
import com.github.braillesystems.learnbraille.data.entities.BrailleDots

object BrailleTrainer {

    fun init(activity: Activity): Unit = UsbParser.init(activity)
    fun trySend(brailleDots: BrailleDots) = UsbParser.trySend(brailleDots)
    fun setSignalHandler(signalHandler: BrailleTrainerSignalHandler): Unit =
        UsbParser.setSignalHandler(signalHandler)
}

interface BrailleTrainerSignalHandler : UsbSignalHandler
