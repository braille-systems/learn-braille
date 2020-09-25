package com.github.braillesystems.learnbraille.ui.views

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.CheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.BrailleDot
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.entities.list
import com.github.braillesystems.learnbraille.data.entities.spelling
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import kotlinx.android.synthetic.main.braille_dots_view.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

class BrailleDotView : androidx.appcompat.widget.AppCompatCheckBox {

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
class BrailleDotsView : ConstraintLayout, KoinComponent {

    private val preferenceRepository: PreferenceRepository by inject()

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

        if (Build.VERSION.SDK_INT >= 22) {
            if (preferenceRepository.traverseDotsInEnumerationOrder) {
                dotButton2.accessibilityTraversalAfter = dotButton1.id
                dotButton3.accessibilityTraversalAfter = dotButton2.id
                dotButton4.accessibilityTraversalAfter = dotButton3.id
                dotButton5.accessibilityTraversalAfter = dotButton4.id
                dotButton6.accessibilityTraversalAfter = dotButton5.id
            } else {
                dotButton4.accessibilityTraversalAfter = dotButton1.id
                dotButton2.accessibilityTraversalAfter = dotButton4.id
                dotButton5.accessibilityTraversalAfter = dotButton2.id
                dotButton3.accessibilityTraversalAfter = dotButton5.id
                dotButton6.accessibilityTraversalAfter = dotButton3.id
            }
        } else {
            Timber.i("API level < 22, unable co control accessibility traversal order")
        }
    }
}

val BrailleDotsView.dotsState: BrailleDotsState
    get() = BrailleDotsState(
        arrayOf(
            dotButton1, dotButton2, dotButton3,
            dotButton4, dotButton5, dotButton6
        )
    )

class BrailleDotsState(val checkBoxes: Array<CheckBox>)

val BrailleDotsState.spelling: String
    get() = brailleDots.spelling

val BrailleDotsState.brailleDots: BrailleDots
    get() = BrailleDots(
        checkBoxes.map(CheckBox::isChecked).toBooleanArray()
    )

fun BrailleDotsState.uncheck() = checkBoxes.forEach { it.isChecked = false }

fun BrailleDotsState.clickable(isClickable: Boolean) =
    checkBoxes.forEach { it.isClickable = isClickable }

fun BrailleDotsState.subscribe(listener: View.OnClickListener) =
    checkBoxes.forEach { it.setOnClickListener(listener) }

fun BrailleDotsState.display(brailleDots: BrailleDots): Unit =
    (checkBoxes zip brailleDots.list)
        .forEach { (checkBox, dot) -> checkBox.isChecked = dot == BrailleDot.F }
        .also { clickable(false) }
