package com.github.braillesystems.learnbraille.ui.screens

import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsView
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsViewMode
import com.github.braillesystems.learnbraille.utils.contextNotNull
import com.github.braillesystems.learnbraille.utils.extendedTextSize
import com.github.braillesystems.learnbraille.utils.forEach
import com.github.braillesystems.learnbraille.utils.setSize
import org.koin.android.ext.android.inject

data class BrailleDotsInfo(
    val view: BrailleDotsView,
    val mode: BrailleDotsViewMode,
    val prev: View,
    val next: View
)

interface FragmentBinding {
    val leftButton: Button? get() = null
    val rightButton: Button? get() = null
    val leftMiddleButton: Button? get() = null
    val rightMiddleButton: Button? get() = null
    val textView: TextView? get() = null
    val brailleDotsInfo: BrailleDotsInfo? get() = null
}

abstract class AbstractFragment : Fragment() {

    protected var binding: FragmentBinding = object : FragmentBinding {}
    protected val preferenceRepository: PreferenceRepository by inject()

    protected fun <B> B.ini(
        getBinding: B.() -> FragmentBinding = {
            object : FragmentBinding {}
        }
    ) = this.also {
        binding = getBinding().apply {
            brailleDotsInfo?.let { (view, mode, prev, next) ->
                view.setMode(mode, prev, next)
            }
            if (preferenceRepository.extendedAccessibilityEnabled) {
                forEach(
                    leftButton,
                    rightButton,
                    leftMiddleButton,
                    rightMiddleButton
                ) {
                    it?.setSize(
                        width = resources
                            .getDimension(R.dimen.side_buttons_extended_width)
                            .toInt()
                    )
                }
                textView?.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP,
                    contextNotNull.extendedTextSize
                )
            }
        }

        iniHelper()
    }

    protected open fun iniHelper() = Unit
}
