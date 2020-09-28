package com.github.braillesystems.learnbraille.ui.screens.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.MarkerSymbol
import com.github.braillesystems.learnbraille.data.entities.Material
import com.github.braillesystems.learnbraille.databinding.FragmentMarkerViewBinding
import com.github.braillesystems.learnbraille.res.showMarkerPrintRules
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.ui.screens.BrailleDotsInfo
import com.github.braillesystems.learnbraille.ui.screens.FragmentBinding
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsViewMode
import com.github.braillesystems.learnbraille.ui.views.display
import com.github.braillesystems.learnbraille.ui.views.dotsState
import com.github.braillesystems.learnbraille.utils.checkedAnnounce
import com.github.braillesystems.learnbraille.utils.getFragmentStringArg
import com.github.braillesystems.learnbraille.utils.getValue
import com.github.braillesystems.learnbraille.utils.parse

class MarkerViewFragment : AbstractFragmentWithHelp(R.string.browser_marker_view_help) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentMarkerViewBinding>(
        inflater,
        R.layout.fragment_marker_view,
        container,
        false
    ).ini {
        object : FragmentBinding {
            override val textView: TextView? = this@ini.infoTextView
            override val rightButton: Button? = this@ini.flipButton
            override val brailleDotsInfo: BrailleDotsInfo? = this@ini.run {
                BrailleDotsInfo(brailleDots, BrailleDotsViewMode.Reading, infoTextView, flipButton)
            }
        }
    }.apply {

        val m: Material = parse(Material.serializer(), getFragmentStringArg("material"))
        require(m.data is MarkerSymbol)

        val text = showMarkerPrintRules.getValue(m.data.type)
        infoTextView.text = text
        checkedAnnounce(text)

        brailleDots.dotsState.display(m.data.brailleDots)
        flipButton.setOnClickListener {
            brailleDots.reflect().display(m.data.brailleDots)
        }

    }.root
}
