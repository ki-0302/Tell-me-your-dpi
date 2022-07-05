package com.maho_ya.tell_me_your_dpi.domain.notification

import com.maho_ya.tell_me_your_dpi.data.prefs.PreferenceStorage
import javax.inject.Inject

interface FirstPostNotificationPermissionUseCase {
    fun isFirstPostNotificationPermission(): Boolean
    fun firstPostNotificationPermissionCompleted()
}

class FirstPostNotificationPermissionUseCaseImpl @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : FirstPostNotificationPermissionUseCase {

    private var isFirstPostNotificationPermission = false

    init {
        isFirstPostNotificationPermission = preferenceStorage.firstPostNotificationsPermission
    }

    override fun isFirstPostNotificationPermission(): Boolean =
        isFirstPostNotificationPermission

    override fun firstPostNotificationPermissionCompleted() {
        preferenceStorage.firstPostNotificationsPermission = false
    }
}
