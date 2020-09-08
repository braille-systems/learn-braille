package com.github.braillesystems.learnbraille.ui.screens.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.MarkerSymbol
import com.github.braillesystems.learnbraille.data.entities.Material
import com.github.braillesystems.learnbraille.databinding.FragmentMarkerViewBinding
import com.github.braillesystems.learnbraille.res.showMarkerPrintRules
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.ui.views.display
import com.github.braillesystems.learnbraille.ui.views.dotsState
import com.github.braillesystems.learnbraille.utils.contextNotNull
import com.github.braillesystems.learnbraille.utils.get
import com.github.braillesystems.learnbraille.utils.getFragmentStringArg
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
    ).also { binding ->

        setHasOptionsMenu(true)

        val m: Material = parse(Material.serializer(), getFragmentStringArg("material"))
        require(m.data is MarkerSymbol)

        binding.infoTextView.text = contextNotNull.showMarkerPrintRules[m.data.type]
        binding.brailleDots.dotsState.display(m.data.brailleDots)

    }.root
}