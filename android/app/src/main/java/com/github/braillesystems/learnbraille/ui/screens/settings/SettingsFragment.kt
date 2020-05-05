package com.github.braillesystems.learnbraille.ui.screens.settings

import android.os.Bundle
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.utils.updateTitle

class SettingsFragment : androidx.preference.PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_hierarchy, rootKey)
        updateTitle(getString(R.string.preferences_actionbar_title))
    }
}