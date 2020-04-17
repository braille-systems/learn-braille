package com.github.braillesystems.learnbraille.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.database.entities.BrailleDot
import com.github.braillesystems.learnbraille.database.entities.BrailleDots
import com.github.braillesystems.learnbraille.database.entities.list
import com.github.braillesystems.learnbraille.database.entities.spelling
import kotlinx.android.synthetic.main.braille_dots.view.*

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
            .inflate(R.layout.braille_dots, this, true)
    }
}

class BrailleDotsState(val checkBoxes: Array<CheckBox>)

val BrailleDotsView.dots: BrailleDotsState
    get() = BrailleDotsState(
        arrayOf(
            dotButton1, dotButton2, dotButton3,
            dotButton4, dotButton5, dotButton6
        )
    )

fun BrailleDotsState.uncheck() = checkBoxes.forEach {
    it.isChecked = false
}

fun BrailleDotsState.clickable(isClickable: Boolean) = checkBoxes.forEach {
    it.isClickable = isClickable
}

val BrailleDotsState.spelling: String
    get() = brailleDots.spelling

val BrailleDotsState.brailleDots: BrailleDots
    get() = BrailleDots(
        checkBoxes.map(CheckBox::isChecked).toBooleanArray()
    )

fun BrailleDotsState.display(brailleDots: BrailleDots): Unit =
    (checkBoxes zip brailleDots.list).forEach { (checkBox, dot) ->
        checkBox.isChecked = dot == BrailleDot.F
    }.also { clickable(false) }
