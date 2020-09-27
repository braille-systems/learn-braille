package com.github.braillesystems.learnbraille.ui.screens.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.Material
import com.github.braillesystems.learnbraille.data.entities.Symbol
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import com.github.braillesystems.learnbraille.databinding.FragmentSymbolViewBinding
import com.github.braillesystems.learnbraille.res.captionRules
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsViewMode
import com.github.braillesystems.learnbraille.ui.views.display
import com.github.braillesystems.learnbraille.ui.views.dotsState
import com.github.braillesystems.learnbraille.utils.applyExtendedAccessibility
import com.github.braillesystems.learnbraille.utils.getFragmentStringArg
import com.github.braillesystems.learnbraille.utils.getValue
import com.github.braillesystems.learnbraille.utils.parse
import org.koin.android.ext.android.inject

class SymbolViewFragment : AbstractFragmentWithHelp(R.string.browser_symbol_view_help) {

    private val preferenceRepository: PreferenceRepository by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentSymbolViewBinding>(
        inflater,
        R.layout.fragment_symbol_view,
        container,
        false
    ).apply {

        setHasOptionsMenu(true)

        val m: Material = parse(Material.serializer(), getFragmentStringArg("material"))
        require(m.data is Symbol)

        letter.letter = m.data.char
        letterCaption.text = captionRules.getValue(m.data)

        brailleDots.mode = BrailleDotsViewMode.Reading
        brailleDots.dotsState.display(m.data.brailleDots)
        flipButton.setOnClickListener {
            brailleDots.reflect().display(m.data.brailleDots)
        }

        if (preferenceRepository.extendedAccessibilityEnabled) {
            applyExtendedAccessibility(rightButton = flipButton)
        }

    }.root
}
