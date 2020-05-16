package com.github.braillesystems.learnbraille.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.User
import com.github.braillesystems.learnbraille.data.entities.UserDao
import com.github.braillesystems.learnbraille.utils.BuzzPattern
import com.github.braillesystems.learnbraille.utils.logged
import timber.log.Timber


interface PreferenceRepository {

    val buzzEnabled: Boolean
    val toastsEnabled: Boolean
    val brailleTrainerEnabled: Boolean get() = false
    val speechRecognitionEnabled: Boolean
    val golubinaBookStepsEnabled: Boolean
    val practiceUseMaterialsPassedInCourse: Boolean

    val currentUserId: Long
    suspend fun getCurrentUser(): User

    val toastDuration get() = Toast.LENGTH_SHORT
    val correctBuzzPattern: BuzzPattern get() = longArrayOf(100, 100, 100, 100, 100, 100)
    val incorrectBuzzPattern: BuzzPattern get() = longArrayOf(0, 200)
}

interface MutablePreferenceRepository : PreferenceRepository

class PreferenceRepositoryImpl(
    private val context: Context,
    private val userDao: UserDao
) : MutablePreferenceRepository {

    override val buzzEnabled: Boolean by logged {
        context.preferences.getBoolean(
            context.getString(R.string.preference_enable_buzz),
            true
        )
    }

    override val toastsEnabled: Boolean by logged {
        context.preferences.getBoolean(
            context.getString(R.string.preference_enable_toasts),
            true
        )
    }

    override val speechRecognitionEnabled: Boolean by logged {
        context.preferences.getBoolean(
            context.getString(R.string.preference_speech_recognition_enabled),
            true // TODO disable for release
        )
    }

    override val golubinaBookStepsEnabled: Boolean by logged {
        context.preferences.getBoolean(
            context.getString(R.string.preference_golubina_book_steps_enabled),
            true
        )
    }

    override val practiceUseMaterialsPassedInCourse: Boolean by logged {
        context.preferences.getBoolean(
            context.getString(R.string.preference_practice_use_passed_material),
            false
        )
    }

    override val currentUserId: Long by logged {
        context.preferences.getLong(
            context.getString(R.string.preference_current_user), 1
        )
    }

    override suspend fun getCurrentUser(): User =
        userDao.getUser(currentUserId)?.also {
            Timber.i("Current user = $it")
        } ?: error("Current user should always exist")
}

private val Context.preferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)
