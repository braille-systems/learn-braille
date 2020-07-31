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
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import com.github.braillesystems.learnbraille.databinding.FragmentStatsBinding
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.utils.Days
import com.github.braillesystems.learnbraille.utils.scope
import com.github.braillesystems.learnbraille.utils.title
import kotlinx.android.synthetic.main.fragment_stats.*
import kotlinx.android.synthetic.main.fragment_stats_table.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class StatsFragment : AbstractFragmentWithHelp(R.string.stats_help) {

    private val preferenceRepository: PreferenceRepository by inject()
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
    ).also { binding ->

        title = getString(R.string.stats_title)
        setHasOptionsMenu(true)

        scope(job).launch {
            val statsList = listOf(stats_week to 7, stats_month to 30)
            for (statsData in statsList) {
                val actions: Actions = actionsRepository.getActionsFrom(Days(statsData.second))
                val cardsMastered =
                    actions.count { it.type is PracticeSubmission && it.type.isCorrect }
                val hintsUsed = actions.count { it.type is PracticeHintAction }
                val totalAttempts = actions.count { it.type is PracticeSubmission }

                val theoryStepsPassed = actions.count { it.type is TheoryPassStep }
                val theoryInputStepsPassed =
                    actions.count { it.type is TheoryPassStep && it.type.isInput }

                statsData.first.apply {
                    practice_mastered_cards.text = """$cardsMastered"""
                    practice_hints.text = """$hintsUsed"""
                    practice_total_attempts.text = """$totalAttempts"""

                    theory_steps_passed.text = """$theoryStepsPassed"""
                    theory_input_steps_passed.text = """$theoryInputStepsPassed"""
                }
            }
        }
    }.root

    override fun onDestroy() = super.onDestroy().also { job.cancel() }
}