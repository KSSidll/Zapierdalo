package com.kssidll.zapierdalo.domain.usecase.steps

import com.kssidll.zapierdalo.domain.repository.StepsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetLastStepsEntityForRunActionUseCase @Inject constructor(
    private val stepsRepository: StepsRepository
) {
    suspend operator fun invoke(
        id: Long,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) = withContext(dispatcher) {
        stepsRepository.getLastEntityForRunAction(id)
    }
}
