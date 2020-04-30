package com.github.braillesystems.learnbraille.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.CheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.types.BrailleDot
import com.github.braillesystems.learnbraille.data.types.BrailleDots
import com.github.braillesystems.learnbraille.data.types.list
import com.github.braillesystems.learnbraille.data.types.spelling
import kotlinx.android.synthetic.main.braille_dots_view.view.*

class BrailleDotView : CheckBox {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet)

    constructor(
        context: Context, attrSet: AttributeSet, defStyleAttr: Int
    ) : super(
        context, attrSet, defStyleAttr
    )

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo?) {
        super.onInitializeAccessibilityNodeInfo(info)
        info?.className = ""
    }
}

/**
 * Represents six Braille dots view.
 */
class BrailleDotsView : ConstraintLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet)

    constructor(
        context: Context, attrSet: AttributeSet, defStyleAttr: Int
    ) : super(
        context, attrSet, defStyleAttr
    )

    init {
        LayoutInflater
            .from(context)
            .inflate(R.layout.braille_dots_view, this, true)
    }
}

val BrailleDotsView.dotsState: BrailleDotsState
    get() = BrailleDotsState(
        arrayOf(
            dotButton1, dotButton2, dotButton3,
            dotButton4, dotButton5, dotButton6
        )
    )

class BrailleDotsState(private val checkBoxes: Array<CheckBox>) {

    val spelling: String
        get() = brailleDots.spelling

    val brailleDots: BrailleDots
        get() = BrailleDots(
            checkBoxes.map(CheckBox::isChecked).toBooleanArray()
        )

    fun uncheck() = checkBoxes.forEach {
        it.isChecked = false
    }

    fun clickable(isClickable: Boolean) = checkBoxes.forEach {
        it.isClickable = isClickable
    }

    fun display(brailleDots: BrailleDots): Unit =
        (checkBoxes zip brailleDots.list).forEach { (checkBox, dot) ->
            checkBox.isChecked = dot == BrailleDot.F
        }.also { clickable(false) }
}
