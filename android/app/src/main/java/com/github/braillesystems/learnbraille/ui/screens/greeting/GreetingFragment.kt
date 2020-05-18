package com.github.braillesystems.learnbraille.ui.screens.greeting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.databinding.FragmentGreetingBinding
import com.github.braillesystems.learnbraille.utils.updateTitle

// TODO decide what to do with this functionality
class GreetingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentGreetingBinding>(
        inflater,
        R.layout.fragment_greeting,
        container,
        false
    ).apply {

        updateTitle(getString(R.string.greeting_title))

    }.root
}
