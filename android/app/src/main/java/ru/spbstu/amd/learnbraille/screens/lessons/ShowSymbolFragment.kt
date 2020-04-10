package ru.spbstu.amd.learnbraille.screens.lessons


import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.entities.ShowSymbol
import ru.spbstu.amd.learnbraille.database.getDBInstance
import ru.spbstu.amd.learnbraille.databinding.FragmentLessonsSymbolBinding
import ru.spbstu.amd.learnbraille.defaultUser
import ru.spbstu.amd.learnbraille.screens.updateTitle
import ru.spbstu.amd.learnbraille.views.clickable
import ru.spbstu.amd.learnbraille.views.display

class ShowSymbolFragment : AbstractLesson(R.string.lessons_help_show_symbol) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsSymbolBinding>(
        inflater,
        R.layout.fragment_lessons_symbol,
        container,
        false
    ).apply {

        updateTitle(getString(R.string.lessons_title_show_symbol))
        setHasOptionsMenu(true)

        val step = getStepArg()
        require(step.data is ShowSymbol)
        titleView.text = step.title
        letter.text = step.data.symbol.symbol.toString()
        brailleDots.dots.apply {
            display(step.data.symbol.brailleDots)
            clickable(false)
        }

        val database = getDBInstance()
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
}
