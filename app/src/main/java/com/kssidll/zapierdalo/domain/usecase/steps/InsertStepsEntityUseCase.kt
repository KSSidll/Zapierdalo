package com.kssidll.zapierdalo.domain.usecase.steps

import com.kssidll.zapierdalo.data.data.StepsEntity
import com.kssidll.zapierdalo.domain.repository.StepsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class InsertStepsEntityUseCase @Inject constructor(
    private val getLastStepsEntityForRunActionUseCase: GetLastStepsEntityForRunActionUseCase,
    private val stepsRepository: StepsRepository
) {
    suspend operator fun invoke(
        entity: StepsEntity,
        dispatcher: CoroutineContext = Dispatchers.IO
    ) = withContext(dispatcher) {
        // ensure previous entity in the chain 'finished'
        val lastEntity = getLastStepsEntityForRunActionUseCase(entity.runActionId)
        if (lastEntity != null && lastEntity.endTimestamp == null) {
            stepsRepository.setEndTimestamp(lastEntity.id, entity.startTimestamp - 1)
        }

        stepsRepository.insert(entity)
    }
}
