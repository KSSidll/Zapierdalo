package com.kssidll.zapierdalo.domain.usecase.runaction

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

class GetAllRunActionEntityUseCase @Inject constructor(
    private val runActionRepository: RunActionRepository
) {
    operator fun invoke(
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<List<RunActionEntity>> {
        return runActionRepository.all()
            .distinctUntilChanged()
            .cancellable()
            .flowOn(dispatcher)
    }
}

class GetAllRunActionUseCase @Inject constructor(
    private val getAllRunActionEntityUseCase: GetAllRunActionEntityUseCase,
    private val getGpsEntityByRunActionUseCase: GetGpsEntityByRunActionUseCase,
    private val getStepsEntityByRunActionUseCase: GetStepsEntityByRunActionUseCase
) {
    operator fun invoke(
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<List<RunAction>> {
        return getAllRunActionEntityUseCase(dispatcher).map { list ->
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
