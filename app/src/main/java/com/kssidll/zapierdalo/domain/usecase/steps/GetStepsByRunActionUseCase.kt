package com.kssidll.zapierdalo.domain.usecase.steps

import com.kssidll.zapierdalo.domain.data.Data
import com.kssidll.zapierdalo.domain.data.Steps
import com.kssidll.zapierdalo.domain.data.toDomain
import com.kssidll.zapierdalo.domain.repository.StepsRepository
import com.kssidll.zapierdalo.domain.usecase.runaction.GetRunActionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetStepsByRunActionUseCase @Inject constructor(
    private val stepsRepository: StepsRepository,
    private val getRunActionUseCase: GetRunActionUseCase,
) {
    operator fun invoke(
        runActionId: Long,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<Data<List<Steps>>> {
        return stepsRepository.byRunActionId(runActionId).map { list ->
            val runAction = getRunActionUseCase(runActionId).first()()!!

            Data.Loaded(
                list.map { entity ->
                    entity.toDomain(runAction)
                }
            )
        }
            .distinctUntilChanged()
            .flowOn(dispatcher)
    }
}
