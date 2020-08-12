package com.maho_ya.domain.review

import com.maho_ya.data.prefs.PreferenceStorage
import com.maho_ya.model.Device
import javax.inject.Inject

interface ShouldLaunchReviewUseCase {
    fun shouldLaunchReview(): Boolean
    fun notifyReviewLaunchAttempted()
}

class ShouldLaunchReviewUseCaseImpl @Inject constructor(
    preferenceStorage: PreferenceStorage
): ShouldLaunchReviewUseCase {

    private var shouldLaunchReview: Boolean = false

    companion object {
        private const val REVIEW_DISPLAY_CYCLE = 10
    }

    init {
        preferenceStorage.appLaunchCount += 1
        shouldLaunchReview = (preferenceStorage.appLaunchCount % REVIEW_DISPLAY_CYCLE == 0)
    }

    override fun shouldLaunchReview(): Boolean =
        shouldLaunchReview

    override fun notifyReviewLaunchAttempted() {
        shouldLaunchReview = false
    }
}