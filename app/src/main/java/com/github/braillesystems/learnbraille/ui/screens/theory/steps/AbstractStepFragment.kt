package com.github.braillesystems.learnbraille.ui.screens.theory.steps

import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.github.braillesystems.learnbraille.COURSE
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.Step
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.ui.screens.HelpMsgId
import com.github.braillesystems.learnbraille.ui.screens.theory.getStepArg
import com.github.braillesystems.learnbraille.ui.screens.theory.toCurrentStep
import com.github.braillesystems.learnbraille.ui.screens.theory.toNextStep
import com.github.braillesystems.learnbraille.ui.screens.theory.toPrevStep
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsView
import com.github.braillesystems.learnbraille.utils.applyExtendedAccessibility
import com.github.braillesystems.learnbraille.utils.navigate
import com.github.braillesystems.learnbraille.utils.title
import org.koin.android.ext.android.inject

interface StepBinding {
    val prevButton: Button? get() = null
    val nextButton: Button? get() = null
    val hintButton: Button? get() = null
    val textView: TextView? get() = null
    val brailleDots: BrailleDotsView? get() = null
}

/**
 * Base class for all steps.
 */
abstract class AbstractStepFragment(helpMsgId: HelpMsgId) : AbstractFragmentWithHelp(helpMsgId) {

    protected val preferenceRepository: PreferenceRepository by inject()

    protected lateinit var step: Step
        private set

    protected lateinit var stepBinding: StepBinding
        private set

    override val helpMsg: String
        get() = getString(R.string.lessons_help_template).format(
            super.helpMsg, getString(R.string.lessons_help_common)
        )

    protected fun <B : ViewDataBinding> B.init(
        titleId: Int,
        binding: B.() -> StepBinding
    ): B = init(this, titleId, binding)

    protected open fun <B> init(
        b: B,
        titleId: Int,
        binding: B.() -> StepBinding
    ): B = b.apply {
        setHasOptionsMenu(true)

        step = getStepArg()
        stepBinding = binding()

        val msg = getString(titleId)
        title = if (preferenceRepository.extendedAccessibilityEnabled) {
            "${step.lessonId} ${step.id} $msg"
        } else {
            "${step.lessonId}.${step.id} $msg"
        }

        stepBinding.run {
            if (preferenceRepository.extendedAccessibilityEnabled) {
                applyExtendedAccessibility(
                    leftButton = prevButton,
                    rightButton = nextButton,
                    leftMiddleButton = hintButton,
                    textView = textView
                )
            }
            prevButton?.setOnClickListener { toPrevStep(step) }
            nextButton?.setOnClickListener { toNextStep(step, markThisAsPassed = true) }
            textView?.movementMethod = ScrollingMovementMethod()
        }
    }

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
