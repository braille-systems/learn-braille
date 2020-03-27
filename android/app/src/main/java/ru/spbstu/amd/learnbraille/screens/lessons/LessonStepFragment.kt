package ru.spbstu.amd.learnbraille.screens.lessons

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.LearnBrailleDatabase
import ru.spbstu.amd.learnbraille.databinding.FragmentLessonStepBinding

class LessonStepFragment : Fragment() {

    private lateinit var viewModel: LessonStepViewModel

    // TODO replace
    private val userId = 1L

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
        val database = LearnBrailleDatabase.getInstance(application)
        val viewModelFactory = LessonStepViewModelFactory(
            application, userId, database.stepDao, database.userPassedStepDao
        ) {
            null
        }
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
            R.id.help -> {
                val action = LessonStepFragmentDirections.actionLessonStepFragmentToHelpFragment()
                action.helpMessage = "HEEELP! LESSONS!"  // TODO lessons help message
                findNavController().navigate(action)
            }
        }
    }
}
