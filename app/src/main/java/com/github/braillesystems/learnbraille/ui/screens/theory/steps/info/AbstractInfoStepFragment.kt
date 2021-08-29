package com.github.braillesystems.learnbraille.ui.screens.theory.steps.info

import androidx.core.text.parseAsHtml
import com.github.braillesystems.learnbraille.data.entities.BaseInfo
import com.github.braillesystems.learnbraille.ui.screens.HelpMsgId
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.AbstractStepFragment
import com.github.braillesystems.learnbraille.utils.checkedAnnounce
import com.github.braillesystems.learnbraille.utils.reduceWhitespaces

abstract class AbstractInfoStepFragment(helpMsgId: HelpMsgId) : AbstractStepFragment(helpMsgId) {

    override fun iniStepHelper() {
        val data = step.data
        require(data is BaseInfo)
        stepBinding.textView?.text = data.text.parseAsHtml()
        checkedAnnounce(data.text.reduceWhitespaces())
    }
}
