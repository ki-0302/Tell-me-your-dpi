package com.maho_ya.di

import com.maho_ya.domain.review.ShouldLaunchReviewUseCase
import com.maho_ya.domain.review.ShouldLaunchReviewUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class ReviewModule {

    @Singleton
    @Binds
    abstract fun provideShouldLaunchReviewUseCase(
        shouldLaunchReviewUseCaseImpl: ShouldLaunchReviewUseCaseImpl
    ): ShouldLaunchReviewUseCase
}
