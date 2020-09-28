package com.github.braillesystems.learnbraille.ui.screens.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.Material
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.databinding.FragmentSymbolViewBinding
import com.github.braillesystems.learnbraille.res.captionRules
import com.github.braillesystems.learnbraille.ui.dotsMode
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.ui.screens.BrailleDotsInfo
import com.github.braillesystems.learnbraille.ui.screens.FragmentBinding
import com.github.braillesystems.learnbraille.ui.showPrint
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsViewMode
import com.github.braillesystems.learnbraille.ui.views.display
import com.github.braillesystems.learnbraille.ui.views.dotsState
import com.github.braillesystems.learnbraille.utils.*

class SymbolViewFragment : AbstractFragmentWithHelp(R.string.browser_symbol_view_help) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentSymbolViewBinding>(
        inflater,
        R.layout.fragment_symbol_view,
        container,
        false
    ).ini {
        object : FragmentBinding {
            override val rightButton: Button? = this@ini.flipButton
            override val brailleDotsInfo: BrailleDotsInfo? = this@ini.run {
                BrailleDotsInfo(brailleDots, BrailleDotsViewMode.Reading, letter, flipButton)
            }
        }
    }.apply {

        val m: Material = parse(Material.serializer(), getFragmentStringArg("material"))
        require(m.data is Symbol)

        letter.letter = m.data.char
        letterCaption.text = captionRules.getValue(m.data)
        checkedAnnounce(showPrint(m.data))

        brailleDots.dotsState.display(m.data.brailleDots)
        checkedToast(dotsMode(brailleDots.mode))
        flipButton.setOnClickListener {
            brailleDots.reflect().display(m.data.brailleDots)
            checkedToast(dotsMode(brailleDots.mode))
        }

    }.root
}
