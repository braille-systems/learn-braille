package ru.spbstu.amd.learnbraille.screens.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.entities.ShowSymbol
import ru.spbstu.amd.learnbraille.database.entities.spelling
import ru.spbstu.amd.learnbraille.database.getDBInstance
import ru.spbstu.amd.learnbraille.databinding.FragmentLessonsShowSymbolBinding
import ru.spbstu.amd.learnbraille.defaultUser
import ru.spbstu.amd.learnbraille.screens.updateTitle
import ru.spbstu.amd.learnbraille.views.display
import ru.spbstu.amd.learnbraille.views.dots
import timber.log.Timber

class ShowSymbolFragment : AbstractLesson(R.string.lessons_help_show_symbol) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsShowSymbolBinding>(
        inflater,
        R.layout.fragment_lessons_show_symbol,
        container,
        false
    ).apply {

        Timber.i("Initialize show symbol fragment")

        updateTitle(getString(R.string.lessons_title_show_symbol))
        setHasOptionsMenu(true)

        val step = getStepArg()
        require(step.data is ShowSymbol)
        titleTextView.text = step.title
        infoTextView.text = step.data.symbol.symbol.toString()
        brailleDots.dots.display(step.data.symbol.brailleDots)

        getDBInstance().apply {
            prevButton.setOnClickListener {
                navigateToPrevStep(stepDao, step)
            }
            nextButton.setOnClickListener {
                navigateToNextStep(
                    stepDao, step, defaultUser,
                    userPassedStepDao
                )
            }
        }

    }.root
}
