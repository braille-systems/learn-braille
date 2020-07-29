package com.github.braillesystems.learnbraille.ui.screens.stats

import android.os.Bundle
import android.text.Spanned
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
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
import com.github.braillesystems.learnbraille.utils.*
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

        val start = 30
        val end = 20

        scope(job).launch {
            val days = 7
            val weekActions = actionsRepository.getActionsFrom(Days(days))
            binding.week.apply {
                text = formatStatsTemplate(days, weekActions)
                setPaddingRelative(start, 0, end, 0)
                if (preferenceRepository.extendedAccessibilityEnabled) {
                    setTextSize(
                        TypedValue.COMPLEX_UNIT_SP,
                        application.extendedTextSize
                    )
                }
            }
        }

        scope(job).launch {
            val days = 30
            val monthActions = actionsRepository.getActionsFrom(Days(days))
            binding.month.apply {
                text = formatStatsTemplate(days, monthActions)
                setPaddingRelative(start, 0, end, 0)
                if (preferenceRepository.extendedAccessibilityEnabled) {
                    setTextSize(
                        TypedValue.COMPLEX_UNIT_SP,
                        application.extendedTextSize
                    )
                }
            }
        }

    }.root

    override fun onDestroy() = super.onDestroy().also { job.cancel() }

    private fun formatStatsTemplate(days: Int, actions: Actions): Spanned =
        getString(R.string.stats_template)
            .format(
                /* days */ days.toString(),
                /* cards passed */ actions.count {
                    it.type is PracticeSubmission && it.type.isCorrect
                },
                /* used hints */ actions.count { it.type is PracticeHintAction },
                /* tries */ actions.count { it.type is PracticeSubmission },
                /* steps passed */ actions.count { it.type is TheoryPassStep },
                /* input steps passed */ actions.count {
                    it.type is TheoryPassStep && it.type.isInput
                }
            )
            .parseAsHtml()
}