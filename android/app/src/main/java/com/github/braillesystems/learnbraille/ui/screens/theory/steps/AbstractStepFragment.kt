package com.github.braillesystems.learnbraille.ui.screens.theory.steps

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import com.github.braillesystems.learnbraille.COURSE_ID
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.Step
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.ui.screens.HelpMsgId
import com.github.braillesystems.learnbraille.ui.screens.theory.toCurrentStep
import com.github.braillesystems.learnbraille.ui.screens.theory.toNextStep
import com.github.braillesystems.learnbraille.ui.screens.theory.toPrevStep
import com.github.braillesystems.learnbraille.utils.navigate
import org.koin.android.ext.android.inject
import com.github.braillesystems.learnbraille.utils.updateTitle as utilUpdateTitle


/**
 * Base class for all steps.
 */
abstract class AbstractStepFragment(helpMsgId: HelpMsgId) : AbstractFragmentWithHelp(helpMsgId) {

    protected val preferenceRepository: PreferenceRepository by inject()
    protected lateinit var step: Step

    override val helpMsg: String
        get() = getString(R.string.lessons_help_template).format(
            super.helpMsg, getString(R.string.lessons_help_common)
        )

    protected fun initialize(step: Step, prevButton: Button?, nextButton: Button?) {
        this.step = step
        setHasOptionsMenu(true)
        if (preferenceRepository.extendedAccessibilityEnabled) {
            prevButton?.let {
                it.width = resources.getDimension(R.dimen.side_buttons_extended_width).toInt()
            }
            nextButton?.let {
                it.width = resources.getDimension(R.dimen.side_buttons_extended_width).toInt()
            }
        }
    }

    protected fun updateTitle(msg: String) {
        utilUpdateTitle("${step.lessonId}.${step.id} $msg")
    }

    protected fun setNextButton(button: Button) {
        button.setOnClickListener {
            toNextStep(step, markThisAsPassed = true)
        }
    }

    protected fun setPrevButton(button: Button) {
        button.setOnClickListener {
            toPrevStep(step)
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
            R.id.current_course_pos -> toCurrentStep(COURSE_ID)
        }
    }

    companion object {
        const val stepArgName = "step"
    }
}
