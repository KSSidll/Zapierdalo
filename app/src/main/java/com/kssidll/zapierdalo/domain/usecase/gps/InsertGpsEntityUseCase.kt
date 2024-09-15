package com.kssidll.zapierdalo.domain.usecase.gps

import com.kssidll.zapierdalo.data.data.GpsEntity
import com.kssidll.zapierdalo.domain.repository.GpsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class InsertGpsEntityUseCase @Inject constructor(
    private val gpsRepository: GpsRepository,
) {
    suspend operator fun invoke(
        entity: GpsEntity,
        dispatcher: CoroutineContext = Dispatchers.IO
    ) = withContext(dispatcher) {
        gpsRepository.insert(entity)
    }
}
