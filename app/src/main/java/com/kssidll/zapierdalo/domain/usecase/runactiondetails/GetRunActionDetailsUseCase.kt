package com.kssidll.zapierdalo.domain.usecase.runactiondetails

import com.kssidll.zapierdalo.domain.data.RunActionDetails
import com.kssidll.zapierdalo.domain.repository.RunActionDetailsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetRunActionDetailsUseCase @Inject constructor(
    private val runActionDetailsRepository: RunActionDetailsRepository
) {
    operator fun invoke(
        id: Long,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<RunActionDetails?> {
        return runActionDetailsRepository.get(id)
            .distinctUntilChanged()
            .cancellable()
            .flowOn(dispatcher)
    }
}
