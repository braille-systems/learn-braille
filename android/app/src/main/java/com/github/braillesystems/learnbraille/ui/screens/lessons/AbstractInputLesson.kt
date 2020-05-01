package com.github.braillesystems.learnbraille.ui.screens.lessons

import android.view.View
import com.github.braillesystems.learnbraille.data.db.LearnBrailleDatabase
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.entities.Step
import com.github.braillesystems.learnbraille.ui.screens.HelpMsgId
import com.github.braillesystems.learnbraille.ui.screens.makeCorrectToast
import com.github.braillesystems.learnbraille.ui.screens.makeHintDotsToast
import com.github.braillesystems.learnbraille.ui.screens.makeIncorrectToast

/**
 * Set `userTouchedDots` to false in `onCreateView`
 */
abstract class AbstractInputLesson(helpMsgId: HelpMsgId) : AbstractLesson(helpMsgId) {

    protected var userTouchedDots: Boolean = false
    protected lateinit var viewModel: InputViewModel

    protected fun getPrevButtonListener(step: Step, userId: Long, database: LearnBrailleDatabase) =
        View.OnClickListener {
            database.apply {
                navigateToPrevStep(
                    current = step,
                    userId = userId,
                    stepDao = stepDao,
                    lastStepDao = userLastStep
                )
            }
        }

    protected fun getToCurrStepListener(userId: Long, database: LearnBrailleDatabase) =
        View.OnClickListener {
            database.apply {
                navigateToCurrentStep(
                    userId = userId,
                    stepDao = stepDao,
                    lastStepDao = userLastStep
                )
            }
        }

    protected fun getEventCorrectObserverBlock(
        step: Step,
        userId: Long,
        database: LearnBrailleDatabase
    ): () -> Unit = {
        makeCorrectToast()
        database.apply {
            navigateToNextStep(
                current = step,
                userId = userId,
                stepDao = stepDao,
                lastStepDao = userLastStep,
                upsd = userPassedStepDao
            )
        }
    }

    protected fun getEventIncorrectObserverBlock(
        step: Step,
        userId: Long,
        database: LearnBrailleDatabase,
        notifyIncorrect: () -> Unit = ::makeIncorrectToast
    ): () -> Unit = {
        if (userTouchedDots) {
            notifyIncorrect()
        } else {
            database.apply {
                navigateToNextStep(
                    current = step,
                    userId = userId,
                    stepDao = stepDao,
                    lastStepDao = userLastStep
                ) {
                    notifyIncorrect()
                }
            }
        }
    }

    protected fun getEventHintObserverBlock(): (BrailleDots) -> Unit = { expectedDots ->
        makeHintDotsToast(expectedDots)
    }

    protected fun getEventPassHintObserverBlock(block: () -> Unit = {}): () -> Unit = block
}
