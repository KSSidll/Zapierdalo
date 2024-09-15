package com.kssidll.zapierdalo.di.module.usecase

import com.kssidll.zapierdalo.domain.repository.StepsRepository
import com.kssidll.zapierdalo.domain.usecase.runaction.GetRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.steps.GetStepsByRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.steps.GetStepsUseCase
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
    fun provideGetStepsUseCase(
        stepsRepository: StepsRepository,
        getRunActionUseCase: GetRunActionUseCase
    ): GetStepsUseCase {
        return GetStepsUseCase(
            stepsRepository = stepsRepository,
            getRunActionUseCase = getRunActionUseCase
        )
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
    fun provideGetStepsByRunActionUseCase(
        stepsRepository: StepsRepository,
        getRunActionUseCase: GetRunActionUseCase
    ): GetStepsByRunActionUseCase {
        return GetStepsByRunActionUseCase(
            stepsRepository = stepsRepository,
            getRunActionUseCase = getRunActionUseCase
        )
    }
}