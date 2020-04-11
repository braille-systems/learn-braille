package ru.spbstu.amd.learnbraille.screens.menu

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import ru.spbstu.amd.learnbraille.R
import ru.spbstu.amd.learnbraille.database.getDBInstance
import ru.spbstu.amd.learnbraille.databinding.FragmentMenuBinding
import ru.spbstu.amd.learnbraille.defaultUser
import ru.spbstu.amd.learnbraille.screens.AbstractFragmentWithHelp
import ru.spbstu.amd.learnbraille.screens.lessons.navigateToCurrentStep
import ru.spbstu.amd.learnbraille.screens.updateTitle

class MenuFragment : AbstractFragmentWithHelp(R.string.menu_help) {

    companion object {
        const val qtResultCode = 0
    }

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

        lessonsButton.setOnClickListener {
            val dataSource = getDBInstance().stepDao
            navigateToCurrentStep(dataSource, defaultUser)
        }

        practiceButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_menuFragment_to_practiceFragment)
        )

        offlinePracticeButton.setOnClickListener {
            try {
                val intent = Intent("com.google.zxing.client.android.SCAN")
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE")
                startActivityForResult(intent, 0)
            } catch (e: Exception) {
                val marketUri = Uri.parse("market://details?id=com.google.zxing.client.android")
                val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
                startActivity(marketIntent)
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
        if (requestCode == qtResultCode) {
            if (resultCode == RESULT_OK) {
                val contents = data?.getStringExtra("SCAN_RESULT")
                Toast.makeText(context, contents, Toast.LENGTH_SHORT).show()
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(context, getString(R.string.msg_cancelled), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
