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
    ).ini().also { binding ->

        scope(job).launch {
            forEach(binding.statsWeek to 7, binding.statsMonth to 30) { (view, days) ->
                val actions: Actions = actionsRepository.actionsFrom(Days(days))

                val cardsMastered =
                    actions.count { it.type is PracticeSubmission && it.type.isCorrect }
                val hintsUsed = actions.count { it.type is PracticeHintAction }
                val totalAttempts = actions.count { it.type is PracticeSubmission }
                view.apply {
                    practiceMasteredCards.text = cardsMastered.toString()
                    practiceHints.text = hintsUsed.toString()
                    practiceTotalAttempts.text = totalAttempts.toString()
                }

                view.theoryStepsPassed.text = actions.count { it.type is TheoryPassStep }.toString()
                view.theoryInputStepsPassed.text = actions.count { it.type is TheoryPassStep && it.type.isInput }.toString()
            }
        }

    }.root

    override fun onDestroy() = super.onDestroy().also { job.cancel() }
}
