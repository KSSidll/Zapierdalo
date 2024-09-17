package com.kssidll.zapierdalo.di.module.usecase

import android.content.Context
import com.kssidll.zapierdalo.domain.usecase.StartRunningActionServiceUseCase
import com.kssidll.zapierdalo.domain.usecase.StopRunningActionServiceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RunningActionServiceUseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideStartRunningActionServiceUseCase(
        @ApplicationContext context: Context
    ): StartRunningActionServiceUseCase {
        return StartRunningActionServiceUseCase(context)
    }

    @Provides
    @ViewModelScoped
    fun provideStopRunningActionServiceUseCase(
        @ApplicationContext context: Context
    ): StopRunningActionServiceUseCase {
        return StopRunningActionServiceUseCase(context)
    }
}