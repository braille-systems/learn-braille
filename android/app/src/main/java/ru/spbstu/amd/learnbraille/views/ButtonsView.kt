package ru.spbstu.amd.learnbraille.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.braille_dots.view.*
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.entities.BrailleDots
import ru.spbstu.amd.learnbraille.database.entities.spelling

class ButtonsView : ConstraintLayout {

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

typealias Dots = Array<CheckBox>

val ButtonsView.dots: Dots
    get() = arrayOf(
        dotButton1, dotButton2, dotButton3,
        dotButton4, dotButton5, dotButton6
    )

fun Dots.uncheck() = forEach {
    it.isChecked = false
}

fun Dots.clickable(isClickable: Boolean) = forEach {
    it.isClickable = isClickable
}

val Dots.spelling: String
    get() = brailleDots.spelling

val Dots.brailleDots: BrailleDots
    get() = BrailleDots(
        map(CheckBox::isChecked).toBooleanArray()
    )
