package com.kssidll.zapierdalo.domain.usecase.steps

import com.kssidll.zapierdalo.domain.repository.StepsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SetStepsCountUseCase @Inject constructor(
    private val stepsRepository: StepsRepository
) {
    suspend operator fun invoke(
        entityId: Long,
        count: Long,
        dispatcher: CoroutineContext = Dispatchers.IO
    ) = withContext(dispatcher) {
        stepsRepository.setStepsCount(entityId, count)
    }
}
