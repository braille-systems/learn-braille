package com.github.braillesystems.learnbraille.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.User
import com.github.braillesystems.learnbraille.data.entities.UserDao


typealias BuzzPattern = LongArray


interface PreferenceRepository {

    val buzzEnabled: Boolean
    val toastsEnabled: Boolean

    val announcementsEnabled: Boolean
    val speechRecognitionEnabled: Boolean

    val currentUserId: Long
    suspend fun currentUser(): User

    /**
     * Return if user with this login exists and operation succeded
     */
    suspend fun changeUser(login: String): Boolean

    val toastDuration get() = Toast.LENGTH_SHORT
    val correctBuzzPattern: BuzzPattern get() = longArrayOf(100, 100, 100, 100, 100, 100)
    val incorrectBuzzPattern: BuzzPattern get() = longArrayOf(0, 200)
}

class PreferenceRepositoryImpl(
    private val context: Context,
    private val userDao: UserDao
) : PreferenceRepository {

    override val buzzEnabled: Boolean
        get() = context.preferences.getBoolean(
            context.getString(R.string.preference_enable_buzz),
            true
        )

    override val toastsEnabled: Boolean
        get() = context.preferences.getBoolean(
            context.getString(R.string.preference_enable_toasts),
            true
        )

    override val announcementsEnabled: Boolean
        get() = context.preferences.getBoolean(
            context.getString(R.string.preference_enable_announcements), true
        )

    override val speechRecognitionEnabled: Boolean
        get() = context.preferences.getBoolean(
            context.getString(R.string.preference_speech_recognition_enabled), true
        )

    override val currentUserId: Long
        get() = context.preferences.getLong(
            context.getString(R.string.preference_current_user), 1
        )

    override suspend fun currentUser(): User =
        userDao.getUser(currentUserId) ?: error("Current user should always exist")

    override suspend fun changeUser(login: String): Boolean =
        userDao.getUser(login)?.let { user ->
            with(context.preferences.edit()) {
                putLong(context.getString(R.string.preference_current_user), user.id)
                apply()
            }
            true
        } ?: false
}

private val Context.preferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)
