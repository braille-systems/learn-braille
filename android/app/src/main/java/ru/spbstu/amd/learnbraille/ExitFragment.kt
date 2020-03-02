package ru.spbstu.amd.learnbraille

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlin.system.exitProcess

class ExitFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_exit, container, false).apply{
        val exitButton: Button = findViewById(R.id.exit_button)
        exitButton.setOnClickListener{
            exitProcess(0)
        }
    }
}
