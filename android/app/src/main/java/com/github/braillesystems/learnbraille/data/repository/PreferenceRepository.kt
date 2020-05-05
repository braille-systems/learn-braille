package com.github.braillesystems.learnbraille.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.User
import com.github.braillesystems.learnbraille.data.entities.UserDao


typealias BuzzPattern = LongArray


interface PreferenceRepository {

    val buzzEnabled: Boolean
    val toastsEnabled: Boolean
    val speechRecognitionEnabled: Boolean

    val currentUserId: Long get() = 1L // TODO
    suspend fun currentUser(): User
    suspend fun changeUser(login: String)

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
        get() = TODO("Not yet implemented")

    override val speechRecognitionEnabled: Boolean
        get() = TODO("Not yet implemented")

    override suspend fun currentUser(): User = User(1, "default", "AAA") // TODO

    override suspend fun changeUser(login: String) {
        TODO("Not yet implemented")
    }
}

private val Context.preferences: SharedPreferences
    get() = getSharedPreferences(
        getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )
