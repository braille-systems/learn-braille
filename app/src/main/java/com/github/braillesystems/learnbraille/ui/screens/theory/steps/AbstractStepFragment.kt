package com.github.braillesystems.learnbraille.ui.screens.theory.steps

import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import com.github.braillesystems.learnbraille.COURSE
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.Step
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.ui.screens.BrailleDotsInfo
import com.github.braillesystems.learnbraille.ui.screens.FragmentBinding
import com.github.braillesystems.learnbraille.ui.screens.HelpMsgId
import com.github.braillesystems.learnbraille.ui.screens.theory.getStepArg
import com.github.braillesystems.learnbraille.ui.screens.theory.toCurrentStep
import com.github.braillesystems.learnbraille.ui.screens.theory.toNextStep
import com.github.braillesystems.learnbraille.ui.screens.theory.toPrevStep
import com.github.braillesystems.learnbraille.utils.checkedAnnounce
import com.github.braillesystems.learnbraille.utils.navigate
import com.github.braillesystems.learnbraille.utils.title

interface StepBinding {
    val prevButton: Button? get() = null
    val nextButton: Button? get() = null
    val hintButton: Button? get() = null
    val flipButton: Button? get() = null
    val textView: TextView? get() = null
    val brailleDotsInfo: BrailleDotsInfo? get() = null
}

/**
 * Base class for all steps.
 */
abstract class AbstractStepFragment(helpMsgId: HelpMsgId) : AbstractFragmentWithHelp(helpMsgId) {

    protected lateinit var step: Step
        private set

    protected lateinit var stepBinding: StepBinding
        private set

    override val helpMsg: String
        get() = getString(R.string.lessons_help_template).format(
            super.helpMsg, getString(R.string.lessons_help_common)
        )

    protected fun <B> B.iniStep(
        titleId: Int,
        getBinding: B.() -> StepBinding
    ) = ini {
        getBinding().run {
            object : FragmentBinding {
                override val leftButton: Button? get() = this@run.prevButton
                override val rightButton: Button? get() = this@run.nextButton
                override val leftMiddleButton: Button? get() = this@run.hintButton
                override val rightMiddleButton: Button? get() = this@run.flipButton
                override val textView: TextView? get() = this@run.textView
                override val brailleDotsInfo: BrailleDotsInfo? get() = this@run.brailleDotsInfo
            }
        }
    }.apply {
        step = getStepArg()
        stepBinding = getBinding()

        val msg = getString(titleId)
        title = if (preferenceRepository.extendedAccessibilityEnabled) {
            "${step.lessonId} ${step.id} $msg"
        } else {
            "${step.lessonId}.${step.id} $msg"
        }

        stepBinding.run {
            prevButton?.setOnClickListener { toPrevStep(step) }
            nextButton?.setOnClickListener { toNextStep(step, markThisAsPassed = true) }
            textView?.movementMethod = ScrollingMovementMethod()
        }

        iniStepHelper()
    }

    protected open fun iniStepHelper() = Unit

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(
            if (preferenceRepository.extendedAccessibilityEnabled) R.menu.steps_menu_hide
            else R.menu.steps_menu,
            menu
        )
    }

    override fun onOptionsItemSelected(item: MenuItem) = false.also {
        when (item.itemId) {
            R.id.help -> navigateToHelp()
            R.id.lessons_list -> navigate(R.id.action_global_lessonsListFragment)
            R.id.current_course_pos -> toCurrentStep(COURSE.id)
        }
    }

    companion object {
        const val stepArgName = "step"
    }
}
