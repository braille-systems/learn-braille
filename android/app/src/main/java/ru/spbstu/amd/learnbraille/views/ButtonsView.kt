package ru.spbstu.amd.learnbraille.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import ru.spbstu.amd.learnbraille.R

class ButtonsView: ConstraintLayout{
    // TODO: @JvmOverloads not used as it makes assumptions that can cause incorrect behavior - not sure if i can use it properly
    constructor(context: Context) : super(context)
    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet)
    constructor(context: Context, attrSet: AttributeSet, defStyleAttr: Int) : super(context, attrSet, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.buttons, this, true)
    }

}
