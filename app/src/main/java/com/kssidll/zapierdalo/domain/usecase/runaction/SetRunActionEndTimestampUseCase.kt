package com.kssidll.zapierdalo.domain.usecase.runaction

import com.kssidll.zapierdalo.domain.repository.RunActionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SetRunActionEndTimestampUseCase @Inject constructor(
    private val runActionRepository: RunActionRepository,
) {
    suspend operator fun invoke(
        entityId: Long,
        endTimestamp: Long?,
        dispatcher: CoroutineContext = Dispatchers.IO
    ) = withContext(dispatcher) {
        runActionRepository.setEndTimestamp(entityId, endTimestamp)
    }
}
