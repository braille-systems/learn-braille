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
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.github.braillesystems.learnbraille.COURSE
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.db.LearnBrailleDatabase
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import com.github.braillesystems.learnbraille.databinding.FragmentMenuBinding
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.ui.screens.theory.toLastCourseStep
import com.github.braillesystems.learnbraille.utils.*
import com.google.android.material.button.MaterialButton
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
    ).also { binding ->

        title = getString(R.string.menu_actionbar_text_template).format(appName)
        setHasOptionsMenu(true)
        requestPermissions()

        val buttons = mutableListOf<MaterialButton>()

        binding.lessonsButton.also {
            buttons += it
        }.setOnClickListener(interruptingOnClickListener {
            toLastCourseStep(COURSE.id)
        })

        binding.practiceButton.also {
            buttons += it
        }.setOnClickListener(interruptingOnClickListener {
            navigate(R.id.action_menuFragment_to_practiceFragment)
        })

        binding.browserButton.also {
            buttons += it
        }.setOnClickListener(interruptingOnClickListener {
            navigate(R.id.action_menuFragment_to_browserFragment)
        })

        binding.statsButton.also {
            buttons += it
        }.setOnClickListener(interruptingOnClickListener {
            navigate(R.id.action_menuFragment_to_statsFragment)
        })

        if (preferenceRepository.additionalQrCodeButtonEnabled) {
            binding.qrPracticeButton.also {
                buttons += it
            }.setOnClickListener {
                try {
                    val intent = Intent("com.google.zxing.client.android.SCAN")
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE")
                    startActivityForResult(intent, qrRequestCode)
                } catch (e: ActivityNotFoundException) {
                    checkedToast(getString(R.string.qr_intent_cancelled))
                    sendMarketIntent("com.google.zxing.client.android")
                }
            }
        } else {
            binding.qrPracticeButton.visibility = View.GONE
        }

        binding.settingsButton.also {
            buttons += it
        }.setOnClickListener {
            navigate(R.id.action_menuFragment_to_settingsFragment)
        }

        if (preferenceRepository.extendedAccessibilityEnabled) {
            binding.exitButton.also {
                buttons += it
            }.setOnClickListener {
                navigate(R.id.action_menuFragment_to_exitFragment)
            }
        } else {
            binding.exitButton.visibility = View.GONE
        }

        colorButtons(buttons)

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
        runIf(preferenceRepository.speechRecognitionEnabled) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return@runIf
            val permission = requireContext().checkSelfPermission(Manifest.permission.RECORD_AUDIO)
            if (permission == PackageManager.PERMISSION_GRANTED) return@runIf
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), recordAudioPermissionCode)
        }
    }

    private fun interruptingOnClickListener(block: (View) -> Unit) =
        View.OnClickListener {
            if (db.isInitialized) block(it)
            else toast(getString(R.string.menu_db_not_initialized_warning))
        }

    private fun colorButtons(buttons: List<MaterialButton>) {
        buttons
            .filterIndexed { i, _ -> i % 2 == 0 }
            .forEach {
                it.setTextColor(
                    ContextCompat.getColor(
                        application, R.color.colorOnSecondary
                    )
                )
                it.setBackgroundColor(
                    ContextCompat.getColor(
                        application, R.color.colorSecondary
                    )
                )
            }

        buttons
            .filterIndexed { i, _ -> i % 2 != 0 }
            .forEach {
                it.setTextColor(
                    ContextCompat.getColor(
                        application, R.color.colorOnPrimary
                    )
                )
                it.setBackgroundColor(
                    ContextCompat.getColor(
                        application, R.color.colorPrimary
                    )
                )
            }
    }

    companion object {
        private const val qrRequestCode = 0
        private const val recordAudioPermissionCode = 29
    }
}
