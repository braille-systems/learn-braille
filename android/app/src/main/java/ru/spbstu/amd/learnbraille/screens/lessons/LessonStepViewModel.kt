package ru.spbstu.amd.learnbraille.screens.lessons

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.spbstu.amd.learnbraille.screens.practice.PracticeViewModel

// TODO add dao's
class LessonStepViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(PracticeViewModel::class.java)) {
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
