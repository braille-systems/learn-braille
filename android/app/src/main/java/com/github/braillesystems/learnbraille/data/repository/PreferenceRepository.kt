package com.github.braillesystems.learnbraille.data.repository

import android.content.Context
import android.widget.Toast
import com.github.braillesystems.learnbraille.R
import com.github.braillesystems.learnbraille.data.entities.User
import com.github.braillesystems.learnbraille.data.entities.UserDao
import com.github.braillesystems.learnbraille.utils.BuzzPattern
import com.github.braillesystems.learnbraille.utils.logged
import com.github.braillesystems.learnbraille.utils.preferences
import timber.log.Timber


interface PreferenceRepository {

    val buzzEnabled: Boolean
    val toastsEnabled: Boolean
    val brailleTrainerEnabled: Boolean get() = false // Uncomment in android manifest when set true
    val speechRecognitionEnabled: Boolean
    val golubinaBookStepsEnabled: Boolean
    val practiceUseMaterialsPassedInCourse: Boolean
    val traverseDotsInEnumerationOrder: Boolean
    val inputOnFlyCheck: Boolean
    val additionalAnnouncementsEnabled: Boolean
    val practiceUseOnlyKnownMaterials: Boolean

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
            false
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
            // to enable recognition, set to `true` and uncomment permission in AndroidManifest
            false // TODO
        )
    }

    override val traverseDotsInEnumerationOrder: Boolean by logged {
        context.preferences.getBoolean(
            context.getString(R.string.preference_traverse_dots_in_enumeration_order),
            false
        )
    }

    override val inputOnFlyCheck: Boolean by logged {
        context.preferences.getBoolean(
            context.getString(R.string.preference_title_on_fly_check),
            false
        )
    }

    override val additionalAnnouncementsEnabled: Boolean by logged {
        context.preferences.getBoolean(
            context.getString(R.string.preference_enable_additional_announcements),
            false
        )
    }

    override val practiceUseOnlyKnownMaterials: Boolean by logged {
        context.preferences.getBoolean(
            context.getString(R.string.preference_practice_use_only_seen_materials),
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
