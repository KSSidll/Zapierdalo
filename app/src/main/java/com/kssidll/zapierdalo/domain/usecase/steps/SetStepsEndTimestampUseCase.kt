package com.kssidll.zapierdalo.domain.usecase.steps

import com.kssidll.zapierdalo.domain.repository.StepsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SetStepsEndTimestampUseCase @Inject constructor(
    private val stepsRepository: StepsRepository
) {
    suspend operator fun invoke(
        entityId: Long,
        endTimestamp: Long?,
        dispatcher: CoroutineContext = Dispatchers.IO
    ) = withContext(dispatcher) {
        stepsRepository.setEndTimestamp(entityId, endTimestamp)
    }
}
