package com.kssidll.zapierdalo.di.module.usecase

import com.kssidll.zapierdalo.domain.repository.RunActionRepository
import com.kssidll.zapierdalo.domain.usecase.runaction.DeleteRunActionEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetAllPagedRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetLatestRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetRunActionEntityUseCase
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
    ): DeleteRunActionEntityUseCase {
        return DeleteRunActionEntityUseCase(runActionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetRunActionEntityUseCase(
        runActionRepository: RunActionRepository,
    ): GetRunActionEntityUseCase {
        return GetRunActionEntityUseCase(runActionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllPagedRunActionUseCase(
        runActionRepository: RunActionRepository,
    ): GetAllPagedRunActionUseCase {
        return GetAllPagedRunActionUseCase(runActionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetLatestRunActionUseCase(
        runActionRepository: RunActionRepository,
    ): GetLatestRunActionUseCase {
        return GetLatestRunActionUseCase(runActionRepository)
    }
}