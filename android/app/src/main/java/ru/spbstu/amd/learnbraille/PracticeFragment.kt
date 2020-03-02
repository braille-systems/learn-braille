package ru.spbstu.amd.learnbraille

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import ru.spbstu.amd.learnbraille.databinding.FragmentPracticeBinding
import kotlin.random.Random

class PracticeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentPracticeBinding>(
        inflater, R.layout.fragment_practice, container, false
    ).apply {
        letter.text = randomLetter()
        mainMenuButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_practiceFragment_to_menuFragment)
        )
        nextButton.setOnClickListener {
            letter.text = randomLetter()
        }
    }.root

    private fun randomLetter() = Random
        .nextInt('A'.toInt(), 'Z'.toInt() + 1)
        .toChar()
        .toString()
}
