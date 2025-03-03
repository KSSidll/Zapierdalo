package com.kssidll.zapierdalo.di.module

import android.content.Context
import com.kssidll.zapierdalo.data.dao.GpsDao
import com.kssidll.zapierdalo.data.dao.RunActionDao
import com.kssidll.zapierdalo.data.dao.RunActionDetailsDao
import com.kssidll.zapierdalo.data.dao.StepsDao
import com.kssidll.zapierdalo.data.database.AppDatabase
import com.kssidll.zapierdalo.data.repository.GpsRepositoryImpl
import com.kssidll.zapierdalo.data.repository.RunActionDetailsRepositoryImpl
import com.kssidll.zapierdalo.data.repository.RunActionRepositoryImpl
import com.kssidll.zapierdalo.data.repository.StepsRepositoryImpl
import com.kssidll.zapierdalo.domain.repository.GpsRepository
import com.kssidll.zapierdalo.domain.repository.RunActionDetailsRepository
import com.kssidll.zapierdalo.domain.repository.RunActionRepository
import com.kssidll.zapierdalo.domain.repository.StepsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase {
        return AppDatabase.build(context)
    }

    @Provides
    fun provideRunActionDao(appDatabase: AppDatabase): RunActionDao {
        return appDatabase.runActionDao()
    }

    @Provides
    fun provideRunActionRepository(runActionDao: RunActionDao): RunActionRepository {
        return RunActionRepositoryImpl(runActionDao)
    }

    @Provides
    fun provideRunActionDetailsDao(appDatabase: AppDatabase): RunActionDetailsDao {
        return appDatabase.runActionDetailsDao()
    }

    @Provides
    fun provideRunActionDetailsRepository(runActionDetailsDao: RunActionDetailsDao): RunActionDetailsRepository {
        return RunActionDetailsRepositoryImpl(runActionDetailsDao)
    }

    @Provides
    fun provideGpsDao(appDatabase: AppDatabase): GpsDao {
        return appDatabase.gpsDao()
    }

    @Provides
    fun provideGpsRepository(gpsDao: GpsDao): GpsRepository {
        return GpsRepositoryImpl(gpsDao)
    }

    @Provides
    fun provideStepsDao(appDatabase: AppDatabase): StepsDao {
        return appDatabase.stepsDao()
    }

    @Provides
    fun provideStepsRepository(stepsDao: StepsDao): StepsRepository {
        return StepsRepositoryImpl(stepsDao)
    }
}
