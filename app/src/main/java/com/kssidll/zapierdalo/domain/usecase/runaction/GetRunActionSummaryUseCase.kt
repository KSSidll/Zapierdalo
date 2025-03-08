package com.kssidll.zapierdalo.domain.usecase.runaction

import com.kssidll.zapierdalo.data.data.view.RunActionSummary
import com.kssidll.zapierdalo.domain.repository.RunActionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetRunActionSummaryUseCase @Inject constructor(
    private val runActionRepository: RunActionRepository,
) {
    operator fun invoke(
        id: Long,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<RunActionSummary?> {
        return runActionRepository.getSummary(id)
            .distinctUntilChanged()
            .cancellable()
            .flowOn(dispatcher)
    }
}
