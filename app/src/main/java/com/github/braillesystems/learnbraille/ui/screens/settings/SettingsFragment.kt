package com.github.braillesystems.learnbraille.ui.screens.settings

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.preference.Preference
import androidx.preference.SwitchPreferenceCompat
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.utils.toast
import timber.log.Timber

class SettingsFragment : androidx.preference.PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_hierarchy, rootKey)

        val preference = findPreference<Preference>(
            getString(R.string.preference_teacher_mode_enabled)
        ) ?: error("No teacher mode preference found")

        preference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                require(preference is SwitchPreferenceCompat)
                require(newValue is Boolean)
                if (newValue) {
                    askCode {
                        val code = 8436
                        if (it != code) {
                            Timber.i("Wrong code: $it, but expected $code")
                            toast(getString(R.string.preference_wrong_code_teacher_mode))
                        } else {
                            Timber.i("Correct code: $code")
                            preference.isChecked = true
                        }
                    }
                    false
                } else {
                    true
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
