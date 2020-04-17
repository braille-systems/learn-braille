package com.github.braillesystems.learnbraille.screens.lessons

import android.view.View
import android.widget.Toast
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.database.LearnBrailleDatabase
import com.github.braillesystems.learnbraille.database.entities.BrailleDots
import com.github.braillesystems.learnbraille.database.entities.Step
import com.github.braillesystems.learnbraille.database.entities.spelling
import com.github.braillesystems.learnbraille.screens.HelpMsgId
import com.github.braillesystems.learnbraille.views.BrailleDotsState
import com.github.braillesystems.learnbraille.views.spelling
import timber.log.Timber

/**
 * Set `userTouchedDots` to false in `onCreateView`
 */
abstract class AbstractInputLesson(helpMsgId: HelpMsgId) : AbstractLesson(helpMsgId) {

    protected var userTouchedDots: Boolean = false

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
            Toast.makeText(
                context, getString(R.string.msg_correct), Toast.LENGTH_SHORT
            ).show()
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
        dots: BrailleDotsState
    ): () -> Unit = {
        database.apply {
            Timber.i("Handle incorrect: entered = ${dots.spelling}")
            if (userTouchedDots) {
                Toast.makeText(
                    context, getString(R.string.msg_incorrect), Toast.LENGTH_SHORT
                ).show()
            } else {
                navigateToNextStep(
                    current = step,
                    userId = userId,
                    stepDao = stepDao,
                    lastStepDao = userLastStep
                ) {
                    Toast.makeText(
                        context, getString(R.string.msg_incorrect), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    protected fun getEventHintObserverBlock(): (BrailleDots) -> Unit = { expectedDots ->
        Timber.i("Handle hint")
        val toast = getString(R.string.practice_hint_template)
            .format(expectedDots.spelling)
        Toast.makeText(context, toast, Toast.LENGTH_LONG).show()
    }

    protected fun getEventPassHintObserverBlock(): () -> Unit = {
        Timber.i("Handle pass hint")
    }
}
