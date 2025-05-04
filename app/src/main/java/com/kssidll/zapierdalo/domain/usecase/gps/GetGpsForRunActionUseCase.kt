package com.kssidll.zapierdalo.domain.usecase.gps

import com.kssidll.zapierdalo.data.data.GpsEntity
import com.kssidll.zapierdalo.domain.repository.GpsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetGpsForRunActionUseCase @Inject constructor(
    private val gpsRepository: GpsRepository
) {
    operator fun invoke(
        runActionId: Long,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<List<GpsEntity>> {
        return gpsRepository.forRunAction(runActionId)
            .distinctUntilChanged()
            .cancellable()
            .flowOn(dispatcher)
    }
}
