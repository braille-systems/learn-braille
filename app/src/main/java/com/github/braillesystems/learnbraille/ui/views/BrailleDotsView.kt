package com.github.braillesystems.learnbraille.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.CheckBox
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.BrailleDot
import com.github.braillesystems.learnbraille.data.entities.BrailleDots
import com.github.braillesystems.learnbraille.data.entities.list
import com.github.braillesystems.learnbraille.data.entities.spelling
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsViewMode.Reading
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsViewMode.Writing
import com.github.braillesystems.learnbraille.utils.chainify
import com.github.braillesystems.learnbraille.utils.forEach
import kotlinx.android.synthetic.main.braille_dots_view.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

@SuppressLint("AppCompatCustomView") // Causes BrailleDotView misplacement
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

enum class BrailleDotsViewMode {
    Reading, // 1, 2, 3 dots are on the LEFT
    Writing  // 1, 2, 3 dots are on the RIGHT
}

val BrailleDotsViewMode.reflected: BrailleDotsViewMode
    get() = when (this) {
        Writing -> Reading
        Reading -> Writing
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

        mode =
            if (preferenceRepository.isWriteModeFirst) Writing
            else Reading
    }

    var mode: BrailleDotsViewMode
        set(value) {
            setDescriptionMode(value)
            reflectChecks()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                setBackgroundMode(value)
            } else {
                Timber.w("Unable to set braille dots background due to low API level")
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                setTraversalMode(value)
            } else {
                Timber.w("API level < 22, unable co control accessibility traversal order")
            }

            field = value
        }

    fun reflect(): BrailleDotsState {
        mode = mode.reflected
        return dotsState
    }

    private fun setDescriptionMode(mode: BrailleDotsViewMode) {
        when (mode) {
            Writing -> forEach(
                dotButton4 to R.string.braille_dot_1,
                dotButton5 to R.string.braille_dot_2,
                dotButton6 to R.string.braille_dot_3,
                dotButton1 to R.string.braille_dot_4,
                dotButton2 to R.string.braille_dot_5,
                dotButton3 to R.string.braille_dot_6
            ) { (dotButton, id) ->
                dotButton.contentDescription = context.getString(id)
            }
            Reading -> forEach(
                dotButton1 to R.string.braille_dot_1,
                dotButton2 to R.string.braille_dot_2,
                dotButton3 to R.string.braille_dot_3,
                dotButton4 to R.string.braille_dot_4,
                dotButton5 to R.string.braille_dot_5,
                dotButton6 to R.string.braille_dot_6
            ) { (dotButton, id) ->
                dotButton.contentDescription = context.getString(id)
            }
        }
    }

    private fun reflectChecks() = forEach(
        dotButton1 to dotButton4,
        dotButton2 to dotButton5,
        dotButton3 to dotButton6
    ) { (left, right) ->
        left.isChecked = right.isChecked.also {
            right.isChecked = left.isChecked
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun setTraversalMode(mode: BrailleDotsViewMode) {
        fun setAfter(next: BrailleDotView, prev: BrailleDotView) {
            next.accessibilityTraversalAfter = prev.id
        }

        when (mode to preferenceRepository.traverseDotsInEnumerationOrder) {
            Writing to true -> chainify(
                dotButton4, dotButton5, dotButton6,
                dotButton1, dotButton2, dotButton3,
                block = ::setAfter
            )
            Writing to false -> chainify(
                dotButton4, dotButton1, dotButton5,
                dotButton2, dotButton6, dotButton3,
                block = ::setAfter
            )
            Reading to true -> chainify(
                dotButton1, dotButton2, dotButton3,
                dotButton4, dotButton5, dotButton6,
                block = ::setAfter
            )
            Reading to false -> chainify(
                dotButton1, dotButton4, dotButton2,
                dotButton5, dotButton3, dotButton6,
                block = ::setAfter
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setBackgroundMode(mode: BrailleDotsViewMode) {
        background = when (mode) {
            Writing -> context.getDrawable(R.drawable.right_border)
            Reading -> context.getDrawable(R.drawable.left_border)
        }
    }
}

val BrailleDotsView.dotsState: BrailleDotsState
    get() = BrailleDotsState(
        when (mode) {
            Writing -> listOf(
                dotButton4, dotButton5, dotButton6,
                dotButton1, dotButton2, dotButton3
            )
            Reading -> listOf(
                dotButton1, dotButton2, dotButton3,
                dotButton4, dotButton5, dotButton6
            )
        }
    )

class BrailleDotsState(val checkBoxes: List<CheckBox>)

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
