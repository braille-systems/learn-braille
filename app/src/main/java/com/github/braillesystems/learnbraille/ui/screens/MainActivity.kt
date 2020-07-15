package com.github.braillesystems.learnbraille.ui.screens

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.ui.brailletrainer.BrailleTrainer
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.i("onCreate")
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.navHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        BrailleTrainer.init(this)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onSupportNavigateUp(): Boolean = try {
        navController.navigateUp()
    } catch (e: IllegalArgumentException) {
        Timber.e(e, "Multitouch navigation")
        false
    }
}
