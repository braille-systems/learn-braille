package com.github.braillesystems.learnbraille.ui.screens.theory.steps

import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import androidx.core.text.parseAsHtml
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.ui.screens.HelpMsgId
import com.github.braillesystems.learnbraille.utils.checkedAnnounce

abstract class AbstractInfoStepFragment(helpMsgId: HelpMsgId) : AbstractStepFragment(helpMsgId) {

    protected fun setText(text: String, infoTextView: TextView) {
        infoTextView.text = text.parseAsHtml()
        infoTextView.movementMethod = ScrollingMovementMethod()
        checkedAnnounce(text)
        if (preferenceRepository.extendedAccessibilityEnabled) {
            infoTextView.textSize = resources.getDimension(R.dimen.lessons_info_extended_text_size)
        }
    }
}
