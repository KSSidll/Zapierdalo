package com.kssidll.zapierdalo.domain.usecase.runaction

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.kssidll.zapierdalo.data.data.RunActionEntity
import com.kssidll.zapierdalo.data.data.totalDistance
import com.kssidll.zapierdalo.data.data.totalSteps
import com.kssidll.zapierdalo.domain.data.RunAction
import com.kssidll.zapierdalo.domain.data.toDomain
import com.kssidll.zapierdalo.domain.repository.RunActionRepository
import com.kssidll.zapierdalo.domain.usecase.gps.GetGpsEntityByRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.steps.GetStepsEntityByRunActionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllPagedRunActionEntityUseCase @Inject constructor(
    private val runActionRepository: RunActionRepository
) {
    operator fun invoke(
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<PagingData<RunActionEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 8,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { runActionRepository.allPaged() }
        ).flow
            .cancellable()
            .distinctUntilChanged()
            .flowOn(dispatcher)
    }
}

class GetAllPagedRunActionUseCase @Inject constructor(
    private val getAllPagedRunActionEntityUseCase: GetAllPagedRunActionEntityUseCase,
    private val getGpsEntityByRunActionUseCase: GetGpsEntityByRunActionUseCase,
    private val getStepsEntityByRunActionUseCase: GetStepsEntityByRunActionUseCase
) {
    operator fun invoke(
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<PagingData<RunAction>> {
        return getAllPagedRunActionEntityUseCase(dispatcher).map { list ->
            list.map { entity ->
                val totalDistance =
                    getGpsEntityByRunActionUseCase(entity.id, dispatcher).map { it.totalDistance() }
                val totalSteps =
                    getStepsEntityByRunActionUseCase(entity.id, dispatcher).map { it.totalSteps() }

                entity.toDomain(
                    totalDistance = totalDistance,
                    totalSteps = totalSteps
                )
            }
        }
            .flowOn(dispatcher)
    }
}
