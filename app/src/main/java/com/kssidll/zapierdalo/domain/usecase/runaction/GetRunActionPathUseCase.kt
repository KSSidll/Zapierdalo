package com.kssidll.zapierdalo.domain.usecase.runaction

import com.kssidll.zapierdalo.domain.usecase.gps.GetGpsForRunActionUseCase
import com.kssidll.zapierdalo.helper.toGeoPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

class GetRunActionPathUseCase @Inject constructor(
    private val getGpsForRunActionUseCase: GetGpsForRunActionUseCase
) {
    operator fun invoke(
        id: Long,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<List<GeoPoint>> {
        return getGpsForRunActionUseCase(id, dispatcher).map {
            it.filter { it.accuracy < 9f }.toGeoPoint()
        }
    }
}
