package ru.spbstu.amd.learnbraille.screens.lessons

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.databinding.FragmentLessonStepBinding

class LessonStepFragment : Fragment() {

    private lateinit var viewModel: LessonStepViewModel

    // TODO should be lessons ecosystem be separate activity?
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonStepBinding>(
        inflater,
        R.layout.fragment_lesson_step,
        container,
        false
    ).apply {

        val application = requireNotNull(activity).application
        val viewModelFactory = LessonStepViewModelFactory(application)
        viewModel = ViewModelProvider(
            this@LessonStepFragment, viewModelFactory
        ).get(LessonStepViewModel::class.java)

        lessonStepViewModel = viewModel
        lifecycleOwner = this@LessonStepFragment

        // TODO set fragment title according to the step title

        setHasOptionsMenu(true)

    }.root

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.help_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = super.onOptionsItemSelected(item).also {
        when (item.itemId) {
            R.id.help -> view
                ?.findNavController()
                ?.navigate(R.id.action_lessonStepFragment_to_helpFragment)
                ?: error("Unable to navigate from lesson fragment to help fragment")
        }
    }
}
