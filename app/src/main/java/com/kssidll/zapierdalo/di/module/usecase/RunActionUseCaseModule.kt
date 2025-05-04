package com.kssidll.zapierdalo.di.module.usecase

import com.kssidll.zapierdalo.domain.repository.GpsRepository
import com.kssidll.zapierdalo.domain.repository.RunActionRepository
import com.kssidll.zapierdalo.domain.usecase.gps.GetGpsForRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.DeleteRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetAllPagedRunActionSummaryUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetRunActionPathUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetRunActionSummaryUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.InsertRunActionEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.SetRunActionEndTimestampUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RunActionUseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideInsertRunActionEntityUseCase(
        runActionRepository: RunActionRepository
    ): InsertRunActionEntityUseCase {
        return InsertRunActionEntityUseCase(runActionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSetRunActionEndTimestampUseCase(
        runActionRepository: RunActionRepository
    ): SetRunActionEndTimestampUseCase {
        return SetRunActionEndTimestampUseCase(runActionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideDeleteRunActionEntityUseCase(
        runActionRepository: RunActionRepository
    ): DeleteRunActionUseCase {
        return DeleteRunActionUseCase(runActionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetRunActionUseCase(
        runActionRepository: RunActionRepository,
    ): GetRunActionUseCase {
        return GetRunActionUseCase(runActionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetRunActionPathUseCase(
        getGpsForRunActionUseCase: GetGpsForRunActionUseCase
    ): GetRunActionPathUseCase {
        return GetRunActionPathUseCase(getGpsForRunActionUseCase)
    }

    @Provides
    @ViewModelScoped
    fun provideGetRunActionSummaryUseCase(
        runActionRepository: RunActionRepository,
    ): GetRunActionSummaryUseCase {
        return GetRunActionSummaryUseCase(runActionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllPagedRunActionSummaryUseCase(
        runActionRepository: RunActionRepository,
    ): GetAllPagedRunActionSummaryUseCase {
        return GetAllPagedRunActionSummaryUseCase(runActionRepository)
    }
}