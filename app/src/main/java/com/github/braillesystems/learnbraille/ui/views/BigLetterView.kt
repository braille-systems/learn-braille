package com.github.braillesystems.learnbraille.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.github.braillesystems.learnbraille.res.inputSymbolPrintRules
import com.github.braillesystems.learnbraille.res.showSymbolPrintRules
import com.github.braillesystems.learnbraille.utils.getValue

@SuppressLint("AppCompatCustomView")
open class BigLetterView : TextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet)

    constructor(
        context: Context, attrSet: AttributeSet, defStyleAttr: Int
    ) : super(
        context, attrSet, defStyleAttr
    )

    var letter: Char
        get() = super.getText().first()
        set(value) = super.setText(value.toString())

    protected fun addContextListener(hintGetter: Context.(Char) -> String) {
        addTextChangedListener(
            afterTextChanged = { text ->
                if (text == null) return@addTextChangedListener
                require(text.length == 1)
                contentDescription = context.hintGetter(text.first())
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
        addContextListener { c ->
            inputSymbolPrintRules.getValue(c)
        }
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
        addContextListener { c ->
            showSymbolPrintRules.getValue(c)
        }
    }
}
