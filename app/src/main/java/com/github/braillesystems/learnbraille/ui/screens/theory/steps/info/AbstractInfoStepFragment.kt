package com.github.braillesystems.learnbraille.ui.screens.theory.steps.info

import androidx.core.text.parseAsHtml
import com.github.braillesystems.learnbraille.data.entities.BaseInfo
import com.github.braillesystems.learnbraille.ui.screens.HelpMsgId
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.AbstractStepFragment
import com.github.braillesystems.learnbraille.ui.screens.theory.steps.StepBinding
import com.github.braillesystems.learnbraille.utils.checkedAnnounce

abstract class AbstractInfoStepFragment(helpMsgId: HelpMsgId) : AbstractStepFragment(helpMsgId) {

    override fun <B> init(b: B, titleId: Int, binding: B.() -> StepBinding): B =
        super
            .init(b, titleId, binding)
            .also {
                val data = step.data
                require(data is BaseInfo)
                stepBinding.textView?.text = data.text.parseAsHtml()
                checkedAnnounce(data.text)
            }
}
