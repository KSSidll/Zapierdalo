package com.kssidll.zapierdalo.di.module.usecase

import com.kssidll.zapierdalo.domain.repository.StepsRepository
import com.kssidll.zapierdalo.domain.usecase.steps.GetLastStepsEntityForRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.steps.InsertStepsEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.steps.SetStepsCountUseCase
import com.kssidll.zapierdalo.domain.usecase.steps.SetStepsEndTimestampUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class StepsUseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideInsertStepsEntityUseCase(
        getLastStepsEntityForRunActionUseCase: GetLastStepsEntityForRunActionUseCase,
        stepsRepository: StepsRepository
    ): InsertStepsEntityUseCase {
        return InsertStepsEntityUseCase(
            getLastStepsEntityForRunActionUseCase = getLastStepsEntityForRunActionUseCase,
            stepsRepository = stepsRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideSetStepsEndTimestampUseCase(
        stepsRepository: StepsRepository,
    ): SetStepsEndTimestampUseCase {
        return SetStepsEndTimestampUseCase(
            stepsRepository = stepsRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideSetStepsCountUseCase(
        stepsRepository: StepsRepository,
    ): SetStepsCountUseCase {
        return SetStepsCountUseCase(
            stepsRepository = stepsRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideGetLastStepsEntityForRunActionUseCase(
        stepsRepository: StepsRepository,
    ): GetLastStepsEntityForRunActionUseCase {
        return GetLastStepsEntityForRunActionUseCase(
            stepsRepository = stepsRepository
        )
    }
}