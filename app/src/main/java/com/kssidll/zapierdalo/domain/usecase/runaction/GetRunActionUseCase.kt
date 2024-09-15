package com.kssidll.zapierdalo.domain.usecase.runaction

import com.kssidll.zapierdalo.domain.data.Data
import com.kssidll.zapierdalo.domain.data.RunAction
import com.kssidll.zapierdalo.domain.data.toDomain
import com.kssidll.zapierdalo.domain.repository.RunActionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRunActionUseCase @Inject constructor(
    private val runActionRepository: RunActionRepository,
) {
    operator fun invoke(
        id: Long,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<Data<out RunAction?>> {
        return runActionRepository.get(id).map { entity ->
            if (entity == null) {
                Data.Loaded<RunAction?>(null)
            } else {
                Data.Loaded(entity.toDomain())
            }
        }
            .distinctUntilChanged()
            .flowOn(dispatcher)
    }
}
