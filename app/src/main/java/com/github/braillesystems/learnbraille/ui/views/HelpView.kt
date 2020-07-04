package com.github.braillesystems.learnbraille.ui.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.parseAsHtml
import com.github.braillesystems.learnbraille.R

class HelpView : LinearLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet)

    constructor(
        context: Context, attrSet: AttributeSet, defStyleAttr: Int
    ) : super(
        context, attrSet, defStyleAttr
    )
}

fun HelpView.setSeparatedText(text: String) {
    val helpItems = text.split('&')
    helpItems.forEach { helpItem ->
        val textView = TextView(context).apply {
            setPaddingRelative(2, 0, 2, 0)
            setTextSize(
                TypedValue.COMPLEX_UNIT_SP,
                context.resources.getDimension(R.dimen.help_message_text_size)
            )
            this.text = helpItem.parseAsHtml()
        }
        addView(textView)
    }
}
