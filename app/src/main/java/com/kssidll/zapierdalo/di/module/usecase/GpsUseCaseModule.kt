package com.kssidll.zapierdalo.di.module.usecase

import com.kssidll.zapierdalo.domain.repository.GpsRepository
import com.kssidll.zapierdalo.domain.usecase.gps.GetAllGpsUseCase
import com.kssidll.zapierdalo.domain.usecase.gps.GetGpsByRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.gps.InsertGpsEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetRunActionUseCase
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
    fun provideGetAllGpsUseCase(
        gpsRepository: GpsRepository,
        getRunActionUseCase: GetRunActionUseCase
    ): GetAllGpsUseCase {
        return GetAllGpsUseCase(
            gpsRepository = gpsRepository,
            getRunActionUseCase = getRunActionUseCase
        )
    }

    @Provides
    @ViewModelScoped
    fun provideGetGpsByRunActionUseCase(
        gpsRepository: GpsRepository,
        getRunActionUseCase: GetRunActionUseCase
    ): GetGpsByRunActionUseCase {
        return GetGpsByRunActionUseCase(
            gpsRepository = gpsRepository,
            getRunActionUseCase = getRunActionUseCase
        )
    }
}