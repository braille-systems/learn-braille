package ru.spbstu.amd.learnbraille.screens.lessons


import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.LearnBrailleDatabase
import ru.spbstu.amd.learnbraille.database.entities.Info
import ru.spbstu.amd.learnbraille.databinding.FragmentLessonsInfoBinding
import ru.spbstu.amd.learnbraille.defaultUser
import ru.spbstu.amd.learnbraille.screens.practice.PracticeFragmentDirections
import ru.spbstu.amd.learnbraille.screens.updateTitle
import timber.log.Timber

class InfoFragment : BaseLessonFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsInfoBinding>(
        inflater,
        R.layout.fragment_lessons_info,
        container,
        false
    ).apply {

        updateTitle(getString(R.string.lessons_title_info))
        setHasOptionsMenu(true);

        val step = stepArg
        require(step.data is Info)
        infoText.text = step.data.text
        titleView.text = step.title

        val application: Application = requireNotNull(activity).application
        val database = LearnBrailleDatabase.getInstance(application)

        prevButton.setOnClickListener {
            navigateToPrevStep(database.stepDao, step)
        }
        nextButton.setOnClickListener {
            navigateToNextStep(
                database.stepDao, step, defaultUser,
                database.userPassedStepDao
            )
        }

    }.root

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.help_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = super.onOptionsItemSelected(item).also {
        when (item.itemId) {
            R.id.help -> {
                Timber.i("Navigate to info help")
                val action = PracticeFragmentDirections.actionGlobalHelpFragment()
                action.helpMessage = getString(R.string.lessons_help_info)
                findNavController().navigate(action)
            }
        }
    }
}
