package com.github.braillesystems.learnbraille.data

import com.github.braillesystems.learnbraille.data.entities.DBid
import com.github.braillesystems.learnbraille.data.entities.User
import com.github.braillesystems.learnbraille.data.repository.PreferenceRepository
import com.github.braillesystems.learnbraille.utils.unreachable

open class UnreachablePreferencesRepository : PreferenceRepository {
    override val buzzEnabled: Boolean
        get() = unreachable
    override val toastsEnabled: Boolean
        get() = unreachable
    override val golubinaBookStepsEnabled: Boolean
        get() = unreachable
    override val slateStylusStepsEnabled: Boolean
        get() = unreachable
    override val traverseDotsInEnumerationOrder: Boolean
        get() = unreachable
    override val inputOnFlyCheck: Boolean
        get() = unreachable
    override val additionalAnnouncementsEnabled: Boolean
        get() = unreachable
    override val practiceUseOnlyKnownMaterials: Boolean
        get() = unreachable
    override val extendedAccessibilityEnabled: Boolean
        get() = unreachable
    override val additionalQrCodeButtonEnabled: Boolean
        get() = unreachable
    override val isWriteModeFirst: Boolean
        get() = unreachable
    override val teacherModeEnabled: Boolean
        get() = unreachable
    override val currentUserId: DBid
        get() = unreachable

    override suspend fun getCurrentUser(): User = unreachable
}
