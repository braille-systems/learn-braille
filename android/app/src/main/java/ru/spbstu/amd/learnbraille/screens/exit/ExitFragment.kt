package ru.spbstu.amd.learnbraille.screens.exit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.databinding.FragmentExitBinding
import kotlin.system.exitProcess

class ExitFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentExitBinding>(
        inflater,
        R.layout.fragment_exit,
        container,
        false
    ).apply {
        exitButton.setOnClickListener {
            exitProcess(0)
        }
        continueButton.setOnClickListener {view: View ->
            Navigation.findNavController(view).navigate(R.id.action_exitFragment_to_menuFragment)
        }
    }.root
}
