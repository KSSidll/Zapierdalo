package com.kssidll.zapierdalo.di.module.usecase

import com.kssidll.zapierdalo.domain.repository.RunActionRepository
import com.kssidll.zapierdalo.domain.usecase.gps.GetGpsEntityByRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetAllPagedRunActionEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetAllPagedRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetAllRunActionEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetAllRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetLatestRunActionEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetLatestRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetRunActionDetailsUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetRunActionEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.InsertRunActionEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.UpdateRunActionEntityUseCase
import com.kssidll.zapierdalo.domain.usecase.steps.GetStepsEntityByRunActionUseCase
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
    fun provideGetRunActionEntityUseCase(
        runActionRepository: RunActionRepository,
    ): GetRunActionEntityUseCase {
        return GetRunActionEntityUseCase(runActionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetRunActionUseCase(
        getRunActionEntityUseCase: GetRunActionEntityUseCase,
        getGpsEntityByRunActionUseCase: GetGpsEntityByRunActionUseCase,
        getStepsEntityByRunActionUseCase: GetStepsEntityByRunActionUseCase
    ): GetRunActionUseCase {
        return GetRunActionUseCase(
            getRunActionEntityUseCase = getRunActionEntityUseCase,
            getGpsEntityByRunActionUseCase = getGpsEntityByRunActionUseCase,
            getStepsEntityByRunActionUseCase = getStepsEntityByRunActionUseCase
        )
    }

    @Provides
    @ViewModelScoped
    fun provideGetRunActionDetailsUseCase(
        getRunActionEntityUseCase: GetRunActionEntityUseCase,
        getGpsEntityByRunActionUseCase: GetGpsEntityByRunActionUseCase,
        getStepsEntityByRunActionUseCase: GetStepsEntityByRunActionUseCase
    ): GetRunActionDetailsUseCase {
        return GetRunActionDetailsUseCase(
            getRunActionEntityUseCase = getRunActionEntityUseCase,
            getGpsEntityByRunActionUseCase = getGpsEntityByRunActionUseCase,
            getStepsEntityByRunActionUseCase = getStepsEntityByRunActionUseCase
        )
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllRunActionEntityUseCase(
        runActionRepository: RunActionRepository
    ): GetAllRunActionEntityUseCase {
        return GetAllRunActionEntityUseCase(runActionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllRunActionUseCase(
        getAllRunActionEntityUseCase: GetAllRunActionEntityUseCase,
        getGpsEntityByRunActionUseCase: GetGpsEntityByRunActionUseCase,
        getStepsEntityByRunActionUseCase: GetStepsEntityByRunActionUseCase
    ): GetAllRunActionUseCase {
        return GetAllRunActionUseCase(
            getAllRunActionEntityUseCase = getAllRunActionEntityUseCase,
            getGpsEntityByRunActionUseCase = getGpsEntityByRunActionUseCase,
            getStepsEntityByRunActionUseCase = getStepsEntityByRunActionUseCase
        )
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllPagedRunActionEntityUseCase(
        runActionRepository: RunActionRepository
    ): GetAllPagedRunActionEntityUseCase {
        return GetAllPagedRunActionEntityUseCase(
            runActionRepository = runActionRepository
        )
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllPagedRunActionUseCase(
        getAllPagedRunActionEntityUseCase: GetAllPagedRunActionEntityUseCase,
        getGpsEntityByRunActionUseCase: GetGpsEntityByRunActionUseCase,
        getStepsEntityByRunActionUseCase: GetStepsEntityByRunActionUseCase
    ): GetAllPagedRunActionUseCase {
        return GetAllPagedRunActionUseCase(
            getAllPagedRunActionEntityUseCase = getAllPagedRunActionEntityUseCase,
            getGpsEntityByRunActionUseCase = getGpsEntityByRunActionUseCase,
            getStepsEntityByRunActionUseCase = getStepsEntityByRunActionUseCase
        )
    }

    @Provides
    @ViewModelScoped
    fun provideGetLatestRunActionEntityUseCase(
        runActionRepository: RunActionRepository,
    ): GetLatestRunActionEntityUseCase {
        return GetLatestRunActionEntityUseCase(runActionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetLatestRunActionUseCase(
        getLatestRunActionEntityUseCase: GetLatestRunActionEntityUseCase,
        getGpsEntityByRunActionUseCase: GetGpsEntityByRunActionUseCase,
        getStepsEntityByRunActionUseCase: GetStepsEntityByRunActionUseCase
    ): GetLatestRunActionUseCase {
        return GetLatestRunActionUseCase(
            getLatestRunActionEntityUseCase = getLatestRunActionEntityUseCase,
            getGpsEntityByRunActionUseCase = getGpsEntityByRunActionUseCase,
            getStepsEntityByRunActionUseCase = getStepsEntityByRunActionUseCase
        )
    }
}