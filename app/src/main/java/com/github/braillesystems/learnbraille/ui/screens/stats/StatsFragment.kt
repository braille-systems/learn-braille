package com.github.braillesystems.learnbraille.ui.screens.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.PracticeHintAction
import com.github.braillesystems.learnbraille.data.entities.PracticeSubmission
import com.github.braillesystems.learnbraille.data.entities.TheoryPassStep
import com.github.braillesystems.learnbraille.data.repository.Actions
import com.github.braillesystems.learnbraille.data.repository.ActionsRepository
import com.github.braillesystems.learnbraille.databinding.FragmentStatsBinding
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.utils.Days
import com.github.braillesystems.learnbraille.utils.forEach
import com.github.braillesystems.learnbraille.utils.scope
import com.github.braillesystems.learnbraille.utils.title
import kotlinx.android.synthetic.main.fragment_stats.*
import kotlinx.android.synthetic.main.fragment_stats_table.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class StatsFragment : AbstractFragmentWithHelp(R.string.stats_help) {

    private val actionsRepository: ActionsRepository by inject()
    private val job = Job()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentStatsBinding>(
        inflater,
        R.layout.fragment_stats,
        container,
        false
    ).also {

        title = getString(R.string.stats_title)
        setHasOptionsMenu(true)

        scope(job).launch {
            forEach(stats_week to 7, stats_month to 30) { (view, days) ->
                val actions: Actions = actionsRepository.actionsFrom(Days(days))

                val cardsMastered =
                    actions.count { it.type is PracticeSubmission && it.type.isCorrect }
                val hintsUsed = actions.count { it.type is PracticeHintAction }
                val totalAttempts = actions.count { it.type is PracticeSubmission }
                view.apply {
                    practice_mastered_cards.text = cardsMastered.toString()
                    practice_hints.text = hintsUsed.toString()
                    practice_total_attempts.text = totalAttempts.toString()
                }

                val theoryStepsPassed = actions.count { it.type is TheoryPassStep }
                val theoryInputStepsPassed =
                    actions.count { it.type is TheoryPassStep && it.type.isInput }
                view.apply {
                    theory_steps_passed.text = theoryStepsPassed.toString()
                    theory_input_steps_passed.text = theoryInputStepsPassed.toString()
                }
            }
        }

    }.root

    override fun onDestroy() = super.onDestroy().also { job.cancel() }
}
