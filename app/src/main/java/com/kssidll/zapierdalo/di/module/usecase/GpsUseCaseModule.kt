package com.kssidll.zapierdalo.di.module.usecase

import com.kssidll.zapierdalo.domain.repository.GpsRepository
import com.kssidll.zapierdalo.domain.usecase.gps.GetGpsEntityByRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.gps.InsertGpsEntityUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class GpsUseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideInsertGpsEntityUseCase(
        gpsRepository: GpsRepository
    ): InsertGpsEntityUseCase {
        return InsertGpsEntityUseCase(gpsRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetGpsEntityByRunActionUseCase(
        gpsRepository: GpsRepository,
    ): GetGpsEntityByRunActionUseCase {
        return GetGpsEntityByRunActionUseCase(gpsRepository)
    }
}