package com.kssidll.zapierdalo.di.module.usecase

import com.kssidll.zapierdalo.domain.repository.RunActionRepository
import com.kssidll.zapierdalo.domain.usecase.runaction.GetLatestRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.InsertRunActionEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.UpdateRunActionEntityUseCase
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
    fun provideUpdateRunActionEntityUseCase(
        runActionRepository: RunActionRepository
    ): UpdateRunActionEntityUseCase {
        return UpdateRunActionEntityUseCase(runActionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetRunActionUseCase(
        runActionRepository: RunActionRepository
    ): GetRunActionUseCase {
        return GetRunActionUseCase(runActionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetLatestRunActionUseCase(
        runActionRepository: RunActionRepository
    ): GetLatestRunActionUseCase {
        return GetLatestRunActionUseCase(runActionRepository)
    }
}