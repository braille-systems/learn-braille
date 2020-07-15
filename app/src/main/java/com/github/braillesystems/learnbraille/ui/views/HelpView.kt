package com.github.braillesystems.learnbraille.ui.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.parseAsHtml
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import com.github.braillesystems.learnbraille.utils.extendedTextSize
import org.koin.core.KoinComponent
import org.koin.core.inject

class HelpView : LinearLayout, KoinComponent {

    private val preferenceRepository: PreferenceRepository by inject()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet)

    constructor(
        context: Context, attrSet: AttributeSet, defStyleAttr: Int
    ) : super(
        context, attrSet, defStyleAttr
    )

    fun setSeparatedText(text: String) {
        val helpItems = text.split('&')
        helpItems.forEach { helpItem ->
            val textView = TextView(context).apply {
                setPaddingRelative(30, 0, 20, 0)
                setTextSize(
                    TypedValue.COMPLEX_UNIT_SP,
                    if (preferenceRepository.extendedAccessibilityEnabled) {
                        context.extendedTextSize
                    } else {
                        context.resources.getDimension(R.dimen.help_message_text_size)
                    }
                )
                this.text = helpItem.parseAsHtml()
            }
            addView(textView)
        }
    }
}
