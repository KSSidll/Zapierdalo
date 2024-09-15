package com.kssidll.zapierdalo.domain.usecase.gps

import com.kssidll.zapierdalo.domain.data.Data
import com.kssidll.zapierdalo.domain.data.Gps
import com.kssidll.zapierdalo.domain.data.toDomain
import com.kssidll.zapierdalo.domain.repository.GpsRepository
import com.kssidll.zapierdalo.domain.usecase.runaction.GetRunActionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllGpsUseCase @Inject constructor(
    private val gpsRepository: GpsRepository,
    private val getRunActionUseCase: GetRunActionUseCase,
) {
    operator fun invoke(
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<Data<List<Gps>>> {
        return gpsRepository.all().map { list ->
            Data.Loaded(
                list.map { entity ->
                    entity.toDomain(getRunActionUseCase(entity.runActionId).first()()!!)
                }
            )
        }
            .distinctUntilChanged()
            .flowOn(dispatcher)
    }
}
