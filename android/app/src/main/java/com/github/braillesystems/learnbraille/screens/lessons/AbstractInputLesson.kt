package com.github.braillesystems.learnbraille.screens.lessons

import android.view.View
import com.github.braillesystems.learnbraille.database.LearnBrailleDatabase
import com.github.braillesystems.learnbraille.database.entities.BrailleDots
import com.github.braillesystems.learnbraille.database.entities.Step
import com.github.braillesystems.learnbraille.screens.HelpMsgId
import com.github.braillesystems.learnbraille.screens.makeCorrectToast
import com.github.braillesystems.learnbraille.screens.makeHintDotsToast
import com.github.braillesystems.learnbraille.screens.makeIncorrectToast
import com.github.braillesystems.learnbraille.views.BrailleDotsState
import com.github.braillesystems.learnbraille.views.spelling
import timber.log.Timber

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
        database.apply {
            Timber.i("Handle correct")
            makeCorrectToast()
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
        dots: BrailleDotsState,
        toastMaker: () -> Unit = ::makeIncorrectToast
    ): () -> Unit = {
        Timber.i("Handle incorrect: entered = ${dots.spelling}")
        if (userTouchedDots) {
            toastMaker()
        } else {
            database.apply {
                navigateToNextStep(
                    current = step,
                    userId = userId,
                    stepDao = stepDao,
                    lastStepDao = userLastStep
                ) {
                    toastMaker()
                }
            }
        }
    }

    protected fun getEventHintObserverBlock(): (BrailleDots) -> Unit = { expectedDots ->
        Timber.i("Handle hint")
        makeHintDotsToast(expectedDots)
    }

    protected fun getEventPassHintObserverBlock(toastMaker: () -> Unit = {}): () -> Unit = {
        Timber.i("Handle pass hint")
        toastMaker()
    }
}
