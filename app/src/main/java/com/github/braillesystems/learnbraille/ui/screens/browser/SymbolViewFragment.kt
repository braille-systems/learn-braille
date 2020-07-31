package com.github.braillesystems.learnbraille.ui.screens.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.Material
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.data.entities.spelling
import com.github.braillesystems.learnbraille.databinding.FragmentSymbolViewBinding
import com.github.braillesystems.learnbraille.res.showSymbolPrintRules
import com.github.braillesystems.learnbraille.ui.views.display
import com.github.braillesystems.learnbraille.ui.views.dotsState
import com.github.braillesystems.learnbraille.utils.*

class SymbolViewFragment : Fragment() {

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

        val m: Material = parse(Material.serializer(), getStringArg("material"))
        require(m.data is Symbol)

        val msg = getString(R.string.browser_represent_template).format(
            application.showSymbolPrintRules[m.data.char].toString(),
            m.data.brailleDots.spelling
        )
        toast(msg)

        binding.letter.text = m.data.char.toString()
        binding.brailleDots.dotsState.display(m.data.brailleDots)

    }.root
}
