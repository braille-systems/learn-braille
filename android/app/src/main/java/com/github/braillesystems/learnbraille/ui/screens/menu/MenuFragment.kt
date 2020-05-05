package com.github.braillesystems.learnbraille.ui.screens.menu

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.db.LearnBrailleDatabase
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import com.github.braillesystems.learnbraille.databinding.FragmentMenuBinding
import com.github.braillesystems.learnbraille.toast
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.utils.sendMarketIntent
import com.github.braillesystems.learnbraille.utils.updateTitle
import org.koin.core.KoinComponent
import org.koin.core.inject


class MenuFragment : AbstractFragmentWithHelp(R.string.menu_help), KoinComponent {

    private val db: LearnBrailleDatabase by inject()
    private val preferences: PreferenceRepository by inject()

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

        updateTitle(getString(R.string.menu_actionbar_text))
        setHasOptionsMenu(true)

        lessonsButton.setOnClickListener(interruptingOnClickListener {
            // TODO navigate to lesson
        })

        practiceButton.setOnClickListener(interruptingOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_practiceFragment)
        })

        qrPracticeButton.setOnClickListener {
            try {
                val intent = Intent("com.google.zxing.client.android.SCAN")
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE")
                startActivityForResult(intent, qrRequestCode)
            } catch (e: ActivityNotFoundException) {
                toast(getString(R.string.qr_intent_cancelled))
                sendMarketIntent("com.google.zxing.client.android")
            }
        }

        stackedHelpButton.setOnClickListener {
            navigateToHelp()
        }

        exitButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_menuFragment_to_exitFragment)
        )

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
                data?.getStringExtra("SCAN_RESULT").toString()  // Not to crash app
            )
        }
    }

    private fun interruptingOnClickListener(block: (View) -> Unit) =
        View.OnClickListener {
            if (db.isInitialized) block(it)
            else toast(getString(R.string.menu_db_not_initialized_warning))
        }

    companion object {
        const val qrRequestCode = 0
    }
}
