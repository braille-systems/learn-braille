package com.github.braillesystems.learnbraille.ui.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.github.braillesystems.learnbraille.ui.PrintMode
import com.github.braillesystems.learnbraille.ui.printString
import timber.log.Timber

open class BigLetterView : TextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet)

    constructor(
        context: Context, attrSet: AttributeSet, defStyleAttr: Int
    ) : super(
        context, attrSet, defStyleAttr
    )

    protected fun addContextListener(mode: PrintMode) {
        addTextChangedListener(
            afterTextChanged = { text ->
                if (text == null) return@addTextChangedListener
                require(text.length == 1)
                contentDescription = context
                    .printString(text.first(), mode)
                    ?: text.first().toLowerCase().toString().also {
                        Timber.e("Symbol intro not found: $text")
                    }
            }
        )
    }
}

class InputBigLetterView : BigLetterView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet)

    constructor(
        context: Context, attrSet: AttributeSet, defStyleAttr: Int
    ) : super(
        context, attrSet, defStyleAttr
    )

    init {
        addContextListener(PrintMode.INPUT)
    }
}

class ShowBigLetterView : BigLetterView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet)

    constructor(
        context: Context, attrSet: AttributeSet, defStyleAttr: Int
    ) : super(
        context, attrSet, defStyleAttr
    )

    init {
        addContextListener(PrintMode.SHOW)
    }
}
