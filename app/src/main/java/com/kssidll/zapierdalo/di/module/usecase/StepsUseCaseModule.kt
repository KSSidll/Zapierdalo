package com.kssidll.zapierdalo.di.module.usecase

import com.kssidll.zapierdalo.domain.repository.StepsRepository
import com.kssidll.zapierdalo.domain.usecase.steps.GetStepsEntityByRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.steps.GetStepsEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.steps.InsertStepsEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.steps.UpdateStepsEntityUseCase
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
        stepsRepository: StepsRepository
    ): InsertStepsEntityUseCase {
        return InsertStepsEntityUseCase(stepsRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetStepsEntityUseCase(
        stepsRepository: StepsRepository,
    ): GetStepsEntityUseCase {
        return GetStepsEntityUseCase(stepsRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideUpdateStepsEntityUseCase(
        stepsRepository: StepsRepository,
    ): UpdateStepsEntityUseCase {
        return UpdateStepsEntityUseCase(
            stepsRepository = stepsRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideGetStepsEntityByRunActionUseCase(
        stepsRepository: StepsRepository,
    ): GetStepsEntityByRunActionUseCase {
        return GetStepsEntityByRunActionUseCase(stepsRepository)
    }
}