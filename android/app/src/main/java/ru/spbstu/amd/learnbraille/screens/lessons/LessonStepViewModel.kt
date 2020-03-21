package ru.spbstu.amd.learnbraille.screens.lessons

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LessonStepViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(LessonStepViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            LessonStepViewModel(application) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}

class LessonStepViewModel(
    application: Application
) : AndroidViewModel(application) {


}
