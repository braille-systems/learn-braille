package com.github.braillesystems.learnbraille.ui.screens.menu

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.COURSE_ID
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.db.LearnBrailleDatabase
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import com.github.braillesystems.learnbraille.databinding.FragmentMenuBinding
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.ui.screens.theory.toLastCourseStep
import com.github.braillesystems.learnbraille.utils.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class MenuFragment : AbstractFragmentWithHelp(R.string.menu_help) {

    private val db: LearnBrailleDatabase by inject()
    private val preferenceRepository: PreferenceRepository by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<FragmentMenuBinding>(
        inflater,
        R.layout.fragment_menu,
        container,
        false
    ).apply {

        title = getString(R.string.menu_actionbar_text_template).format(appName)
        setHasOptionsMenu(true)
        requestPermissions()

        lessonsButton.setOnClickListener(interruptingOnClickListener {
            toLastCourseStep(COURSE_ID)
        })

        practiceButton.setOnClickListener(interruptingOnClickListener {
            navigate(R.id.action_menuFragment_to_practiceFragment)
        })

        qrPracticeButton.setOnClickListener {
            try {
                val intent = Intent("com.google.zxing.client.android.SCAN")
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE")
                startActivityForResult(intent, qrRequestCode)
            } catch (e: ActivityNotFoundException) {
                checkedToast(getString(R.string.qr_intent_cancelled))
                sendMarketIntent("com.google.zxing.client.android")
            }
        }

        settingsButton.setOnClickListener {
            navigate(R.id.action_menuFragment_to_settingsFragment)
        }

        exitButton.setOnClickListener {
            navigate(R.id.action_menuFragment_to_exitFragment)
        }

    }.root

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            qrRequestCode -> processQrResult(resultCode, data)
        }
    }

    private fun processQrResult(resultCode: Int, data: Intent?) {
        when (resultCode) {
            RESULT_OK -> toast(
                data?.getStringExtra("SCAN_RESULT")
                    ?: getString(R.string.menu_qr_empty_result).also {
                        Timber.e("QR: empty result with OK code")
                    }
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            recordAudioPermissionCode -> if (grantResults.first() != PackageManager.PERMISSION_GRANTED) {
                toast(getString(R.string.voice_record_denial))
            }
        }
    }

    private fun requestPermissions() {
        executeIf(preferenceRepository.speechRecognitionEnabled) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return@executeIf
            val permission = requireContext().checkSelfPermission(Manifest.permission.RECORD_AUDIO)
            if (permission == PackageManager.PERMISSION_GRANTED) return@executeIf
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), recordAudioPermissionCode)
        }
    }

    private fun interruptingOnClickListener(block: (View) -> Unit) =
        View.OnClickListener {
            if (db.isInitialized) block(it)
            else toast(getString(R.string.menu_db_not_initialized_warning))
        }

    companion object {
        private const val qrRequestCode = 0
        private const val recordAudioPermissionCode = 29
    }
}
