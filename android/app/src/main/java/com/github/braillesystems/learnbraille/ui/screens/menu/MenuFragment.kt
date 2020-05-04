package com.github.braillesystems.learnbraille.ui.screens.menu

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.TOAST_DURATION
import com.github.braillesystems.learnbraille.data.db.LearnBrailleDatabase
import com.github.braillesystems.learnbraille.databinding.FragmentMenuBinding
import com.github.braillesystems.learnbraille.ui.screens.AbstractFragmentWithHelp
import com.github.braillesystems.learnbraille.userId
import com.github.braillesystems.learnbraille.utils.application
import com.github.braillesystems.learnbraille.utils.sendMarketIntent
import com.github.braillesystems.learnbraille.utils.updateTitle

class MenuFragment : AbstractFragmentWithHelp(R.string.menu_help) {

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
//            getDBInstance().apply {
//                navigateToLastStep(application.userId, stepDao, userLastStep)
//            }
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
                Toast.makeText(
                    context,
                    getString(R.string.qr_intent_cancelled),
                    TOAST_DURATION
                ).show()
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
            RESULT_OK ->
                Toast.makeText(
                    context,
                    data?.getStringExtra("SCAN_RESULT"),
                    TOAST_DURATION
                ).show()
        }
    }

    private fun interruptingOnClickListener(block: (View) -> Unit) =
        View.OnClickListener {
            if (LearnBrailleDatabase.isInitialized) block(it)
            else {
                Toast.makeText(
                    context, getString(R.string.menu_db_not_initialized_warning), TOAST_DURATION
                ).show()
            }
        }

    companion object {
        const val qrRequestCode = 0
    }
}
