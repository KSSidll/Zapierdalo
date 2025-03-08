package com.kssidll.zapierdalo.domain.usecase.runaction

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kssidll.zapierdalo.data.data.view.RunActionSummary
import com.kssidll.zapierdalo.domain.repository.RunActionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllPagedRunActionSummaryUseCase @Inject constructor(
    private val runActionRepository: RunActionRepository
) {
    operator fun invoke(
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<PagingData<RunActionSummary>> {
        return Pager(
            config = PagingConfig(
                pageSize = 8,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { runActionRepository.allSummaryPaged() }
        ).flow
            .cancellable()
            .distinctUntilChanged()
            .flowOn(dispatcher)
    }
}
