package com.kssidll.zapierdalo.domain.usecase.steps

import com.kssidll.zapierdalo.data.data.StepsEntity
import com.kssidll.zapierdalo.domain.repository.StepsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetStepsEntityByRunActionUseCase @Inject constructor(
    private val stepsRepository: StepsRepository
) {
    operator fun invoke(
        runActionId: Long,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<List<StepsEntity>> {
        return stepsRepository.byRunActionId(runActionId)
            .distinctUntilChanged()
            .cancellable()
            .flowOn(dispatcher)
    }
}
