package com.github.braillesystems.learnbraille.ui.screens.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.Material
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.databinding.FragmentSymbolViewBinding
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.ui.views.display
import com.github.braillesystems.learnbraille.ui.views.dotsState
import com.github.braillesystems.learnbraille.utils.getFragmentStringArg
import com.github.braillesystems.learnbraille.utils.parse
import com.github.braillesystems.learnbraille.utils.title

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
    ).also { binding ->

        title = getString(R.string.browser_symbol_view_title)
        setHasOptionsMenu(true)

        val m: Material = parse(Material.serializer(), getFragmentStringArg("material"))
        require(m.data is Symbol)

        binding.letter.text = m.data.char.toString()
        binding.brailleDots.dotsState.display(m.data.brailleDots)

    }.root
}
