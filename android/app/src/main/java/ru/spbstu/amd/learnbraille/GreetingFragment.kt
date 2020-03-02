package ru.spbstu.amd.learnbraille

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import ru.spbstu.amd.learnbraille.databinding.FragmentGreetingBinding

class GreetingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentGreetingBinding>(
        inflater, R.layout.fragment_greeting, container, false
    ).apply {
        startButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_greetingFragment_to_menuFragment)
        )
        exitButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_greetingFragment_to_exitFragment)
        )
    }.root
}
