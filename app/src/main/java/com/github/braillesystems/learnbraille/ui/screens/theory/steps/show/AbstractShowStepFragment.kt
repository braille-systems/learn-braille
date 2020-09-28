package com.github.braillesystems.learnbraille.ui.screens.theory.steps.show

import com.github.braillesystems.learnbraille.data.entities.BaseShow
import com.github.braillesystems.learnbraille.ui.screens.HelpMsgId
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.AbstractStepFragment
import com.github.braillesystems.learnbraille.ui.views.display
import com.github.braillesystems.learnbraille.ui.views.dotsState

abstract class AbstractShowStepFragment(helpMsgId: HelpMsgId) : AbstractStepFragment(helpMsgId) {

    override fun iniStepHelper() {
        val data = step.data
        require(data is BaseShow)
        stepBinding.brailleDotsInfo?.view?.dotsState?.display(data.brailleDots)
            ?: error("Show step should have braille dots")
        stepBinding.flipButton?.setOnClickListener {
            stepBinding.brailleDotsInfo?.view?.reflect()?.display(data.brailleDots)
        }
    }
}
