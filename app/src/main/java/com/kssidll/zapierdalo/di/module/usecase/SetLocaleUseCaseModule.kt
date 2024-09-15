package com.kssidll.zapierdalo.di.module.usecase

import com.kssidll.zapierdalo.domain.usecase.SetLocaleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class SetLocaleUseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideSetLocaleUseCase(): SetLocaleUseCase {
        return SetLocaleUseCase()
    }
}