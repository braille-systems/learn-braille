package com.github.braillesystems.learnbraille.ui.screens.browser

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.*
import com.github.braillesystems.learnbraille.databinding.FragmentMarkerViewBinding
import com.github.braillesystems.learnbraille.res.musicalNotesTypes
import com.github.braillesystems.learnbraille.res.showMarkerPrintRules
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.ui.screens.BrailleDotsInfo
import com.github.braillesystems.learnbraille.ui.screens.FragmentBinding
import com.github.braillesystems.learnbraille.ui.views.BrailleDotsViewMode
import com.github.braillesystems.learnbraille.ui.views.display
import com.github.braillesystems.learnbraille.ui.views.dotsState
import com.github.braillesystems.learnbraille.utils.*

enum class NoteDuration(val titleStrId: Int, val dot3: Boolean, val dot6: Boolean) {
    EIGHTH(titleStrId = R.string.note_duration_8th, dot3 = false, dot6 = false),
    QUARTER(titleStrId = R.string.note_duration_4th, dot3 = false, dot6 = true),
    HALF(titleStrId = R.string.note_duration_half, dot3 = true, dot6 = false),
    FULL(titleStrId = R.string.note_duration_full, dot3 = true, dot6 = true);

    fun modifiedNote(note: BrailleDots): BrailleDots {
        return BrailleDots(
            note.b1, note.b2, BrailleDot.valueOf(dot3),
            note.b4, note.b5, BrailleDot.valueOf(dot6)
        )
    }
}

class MarkerViewFragment : AbstractFragmentWithHelp(R.string.browser_marker_view_help) {

    private fun chooseNoteDuration(block: (NoteDuration) -> Unit, brailleDots: BrailleDots) {
        val input = RadioGroup(context)
        var noteDuration = NoteDuration.EIGHTH
        for (duration in NoteDuration.values()) {
            input.addView(RadioButton(context).also {
                it.text = getString(R.string.browser_represent_template).format(
                    getString(duration.titleStrId),
                    duration.modifiedNote(brailleDots).spelling
                )
                it.setOnClickListener { noteDuration = duration }
            })
        }

        AlertDialog.Builder(context)
            .setTitle(getString(R.string.browser_note_duration_dialog_title))
            .setView(input)
            .setPositiveButton(getString(R.string.browser_note_duration_dialog_confirm)) { _, _ ->
                block(noteDuration)
            }
            .show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentMarkerViewBinding>(
        inflater,
        R.layout.fragment_marker_view,
        container,
        false
    ).ini {
        object : FragmentBinding {
            override val textView: TextView? = this@ini.infoTextView
            override val rightButton: Button? = this@ini.flipButton
            override val leftButton: Button? = this@ini.durationButton
            override val brailleDotsInfo: BrailleDotsInfo? = this@ini.run {
                BrailleDotsInfo(brailleDots, BrailleDotsViewMode.Reading, infoTextView, flipButton)
            }
        }
    }.apply {

        val m: Material = parse(Material.serializer(), getFragmentStringArg("material"))
        require(m.data is MarkerSymbol)

        val baseText = showMarkerPrintRules.getValue(m.data.type)
        var text = baseText

        var dots: BrailleDots = m.data.brailleDots
        if (m.data.type in musicalNotesTypes) {
            val noteDurationTemplate = getString(R.string.note_title_duration_template)
            text += noteDurationTemplate.format(getString(R.string.note_duration_8th))
            durationButton.visibility = View.VISIBLE
            durationButton.setOnClickListener {
                chooseNoteDuration({
                    dots = it.modifiedNote(m.data.brailleDots)
                    brailleDots.dotsState.display(dots)
                    text = baseText + noteDurationTemplate.format(getString(it.titleStrId))
                    infoTextView.text = text
                    checkedAnnounce(text)
                }, brailleDots = dots)
            }
        }

        infoTextView.text = text
        checkedAnnounce(text)

        brailleDots.dotsState.display(dots)
        flipButton.setOnClickListener { brailleDots.reflect().display(dots) }

    }.root
}
