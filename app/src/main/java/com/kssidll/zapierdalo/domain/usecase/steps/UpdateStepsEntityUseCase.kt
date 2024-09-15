package com.kssidll.zapierdalo.domain.usecase.steps

import com.kssidll.zapierdalo.data.data.StepsEntity
import com.kssidll.zapierdalo.domain.repository.StepsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class UpdateStepsEntityUseCase @Inject constructor(
    private val stepsRepository: StepsRepository
) {
    suspend operator fun invoke(
        entity: StepsEntity,
        dispatcher: CoroutineContext = Dispatchers.IO
    ) = withContext(dispatcher) {
        stepsRepository.update(entity)
    }
}
