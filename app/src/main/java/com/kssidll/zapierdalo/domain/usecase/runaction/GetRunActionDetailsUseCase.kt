package com.kssidll.zapierdalo.domain.usecase.runaction

import com.kssidll.zapierdalo.data.data.totalDistance
import com.kssidll.zapierdalo.data.data.totalSteps
import com.kssidll.zapierdalo.domain.data.Data
import com.kssidll.zapierdalo.domain.data.RunActionDetails
import com.kssidll.zapierdalo.domain.data.toDomain
import com.kssidll.zapierdalo.domain.usecase.gps.GetGpsEntityByRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.steps.GetStepsEntityByRunActionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRunActionDetailsUseCase @Inject constructor(
    private val getRunActionEntityUseCase: GetRunActionEntityUseCase,
    private val getGpsEntityByRunActionUseCase: GetGpsEntityByRunActionUseCase,
    private val getStepsEntityByRunActionUseCase: GetStepsEntityByRunActionUseCase
) {
    operator fun invoke(
        id: Long,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<Data<out RunActionDetails?>> {
        return getRunActionEntityUseCase(id, dispatcher).map { runActionEntity ->
            val entity = runActionEntity?.let { entity ->
                val gpsEntity = getGpsEntityByRunActionUseCase(entity.id, dispatcher)
                val totalDistance = gpsEntity.map { it.totalDistance() }
                val gpsPoints = gpsEntity.map { gpsEntityList -> gpsEntityList.map { it.toDomain() } }

                val totalSteps =
                    getStepsEntityByRunActionUseCase(entity.id, dispatcher).map { it.totalSteps() }

                entity.toDomain(
                    totalDistance = totalDistance,
                    totalSteps = totalSteps,
                    gpsPoints = gpsPoints
                )
            }

            Data.Loaded(entity)
        }
            .flowOn(dispatcher)
    }
}
