package ru.spbstu.amd.learnbraille.screens.greeting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.databinding.FragmentGreetingBinding
import ru.spbstu.amd.learnbraille.screens.updateTitle

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

        startButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_greetingFragment_to_menuFragment)
        )

    }.root
}
