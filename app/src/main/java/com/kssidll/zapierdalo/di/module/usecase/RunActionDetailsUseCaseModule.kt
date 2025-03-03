package com.kssidll.zapierdalo.di.module.usecase

import com.kssidll.zapierdalo.domain.repository.RunActionDetailsRepository
import com.kssidll.zapierdalo.domain.usecase.runactiondetails.GetRunActionDetailsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RunActionDetailsUseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideGetRunActionDetailsUseCase(
        runActionDetailsRepository: RunActionDetailsRepository
    ): GetRunActionDetailsUseCase {
        return GetRunActionDetailsUseCase(runActionDetailsRepository)
    }
}