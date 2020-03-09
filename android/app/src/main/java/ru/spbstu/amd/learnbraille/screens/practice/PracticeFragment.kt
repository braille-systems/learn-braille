package ru.spbstu.amd.learnbraille.screens.practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.databinding.FragmentPracticeBinding
import timber.log.Timber

class PracticeFragment : Fragment() {

    private lateinit var viewModel: PracticeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentPracticeBinding>(
        inflater, R.layout.fragment_practice, container, false
    ).apply {

        viewModel = ViewModelProvider(this@PracticeFragment).get(PracticeViewModel::class.java)

        viewModel.dotCheckBoxes = arrayOf(
            dotButton1, dotButton2, dotButton3,
            dotButton4, dotButton5, dotButton6
        )

        practiceViewModel = viewModel
        lifecycleOwner = this@PracticeFragment

        mainMenuButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_practiceFragment_to_menuFragment
            )
        )

        viewModel.eventCorrect.observe(this@PracticeFragment, Observer {
            if (!it) {
                return@Observer
            }
            Toast.makeText(context, "Correct!", Toast.LENGTH_SHORT).show()
            Timber.i("Handle correct")
            viewModel.onCorrectComplete()
        })

        viewModel.eventIncorrect.observe(this@PracticeFragment, Observer {
            if (!it) {
                return@Observer
            }
            Toast.makeText(context, "Incorrect!", Toast.LENGTH_SHORT).show()
            Timber.i("Handle incorrect")
            viewModel.onIncorrectComplete()
        })

    }.root
}


