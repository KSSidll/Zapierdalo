package com.kssidll.zapierdalo.domain.usecase.runaction

import com.kssidll.zapierdalo.data.data.RunActionEntity
import com.kssidll.zapierdalo.domain.repository.RunActionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DeleteRunActionEntityUseCase @Inject constructor(
    private val runActionRepository: RunActionRepository,
) {
    suspend operator fun invoke(
        entity: RunActionEntity,
        dispatcher: CoroutineContext = Dispatchers.IO
    ) = withContext(dispatcher) {
        runActionRepository.delete(entity)
    }

    suspend operator fun invoke(
        entityId: Long,
        dispatcher: CoroutineContext = Dispatchers.IO
    ) = withContext(dispatcher) {
        runActionRepository.delete(entityId)
    }
}
