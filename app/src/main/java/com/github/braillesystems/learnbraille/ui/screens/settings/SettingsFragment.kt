package com.github.braillesystems.learnbraille.ui.screens.settings

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.SwitchPreferenceCompat
import com.github.braillesystems.learnbraille.COURSE
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.CurrentStep
import com.github.braillesystems.learnbraille.data.repository.MutableTheoryRepository
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import com.github.braillesystems.learnbraille.utils.contextNotNull
import com.github.braillesystems.learnbraille.utils.preferences
import com.github.braillesystems.learnbraille.utils.toast
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class SettingsFragment : androidx.preference.PreferenceFragmentCompat() {

    private val preferenceRepository: PreferenceRepository by inject()
    private val theoryRepository: MutableTheoryRepository by inject()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_hierarchy, rootKey)

        val preference = findPreference<Preference>(
            getString(R.string.preference_teacher_mode_enabled)
        ) ?: error("No teacher mode preference found")

        preference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                require(preference is SwitchPreferenceCompat)
                require(newValue is Boolean)

                val saveCurrStepPreference =
                    "save_curr_step_teacher_mode_${preferenceRepository.currentUserId}"
                if (newValue) {
                    askCode {
                        val code = 8436
                        if (it != code) {
                            Timber.i("Wrong code: $it, but expected $code")
                            toast(getString(R.string.preference_wrong_code_teacher_mode))
                        } else {
                            Timber.i("Correct code: $code")
                            lifecycleScope.launch {
                                val curr = theoryRepository.currentStep(COURSE.id)
                                with(contextNotNull.preferences.edit()) {
                                    putString(
                                        saveCurrStepPreference,
                                        listOf(curr.courseId, curr.lessonId, curr.id)
                                            .joinToString(separator = ",")
                                    )
                                    apply()
                                }
                                preference.isChecked = true
                            }
                        }
                    }
                    false
                } else {
                    lifecycleScope.launch {
                        contextNotNull.preferences
                            .getString(saveCurrStepPreference, null)
                            ?.split(",")
                            ?.map { it.toLong() }
                            ?.let { (courseId, lessonId, stepId) ->
                                theoryRepository.setCurrentStep(
                                    CurrentStep(
                                        preferenceRepository.currentUserId,
                                        courseId,
                                        lessonId,
                                        stepId
                                    )
                                )
                            }
                        preference.isChecked = false
                    }
                    false
                }
            }
    }

    private fun askCode(block: (Int?) -> Unit) {
        val input = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
        }
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.preference_code_dialog_title))
            .setView(input)
            .setPositiveButton(getString(R.string.preference_code_dialog_ok)) { _, _ ->
                block(input.text.toString().toIntOrNull())
            }
            .setNegativeButton(getString(R.string.preference_code_dialog_cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }
}
