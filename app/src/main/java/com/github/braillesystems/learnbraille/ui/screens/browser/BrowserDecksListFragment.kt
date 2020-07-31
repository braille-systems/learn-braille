package com.github.braillesystems.learnbraille.ui.screens.browser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.databinding.FragmentBrowserDecksListBinding

class BrowserDecksListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentBrowserDecksListBinding>(
        inflater,
        R.layout.fragment_browser_decks_list,
        container,
        false
    ).also { binding ->

        // TODO

    }.root
}
