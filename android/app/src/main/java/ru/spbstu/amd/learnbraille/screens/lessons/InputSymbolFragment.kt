package ru.spbstu.amd.learnbraille.screens.lessons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.entities.InputSymbol
import ru.spbstu.amd.learnbraille.database.getDBInstance
import ru.spbstu.amd.learnbraille.databinding.FragmentLessonsInputSymbolBinding
import ru.spbstu.amd.learnbraille.defaultUser
import ru.spbstu.amd.learnbraille.screens.updateTitle
import ru.spbstu.amd.learnbraille.views.display
import ru.spbstu.amd.learnbraille.views.dots
import timber.log.Timber

class InputSymbolFragment : AbstractLesson(R.string.lessons_help_input_symbol) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentLessonsInputSymbolBinding>(
        inflater,
        R.layout.fragment_lessons_input_symbol,
        container,
        false
    ).apply {

        Timber.i("Initialize input symbol fragment")

        updateTitle(getString(R.string.lessons_title_input_symbol))
        setHasOptionsMenu(true)

        val step = getStepArg()
        require(step.data is InputSymbol)
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
