package com.kssidll.zapierdalo.domain.usecase

import android.content.Context
import com.kssidll.zapierdalo.service.RunningActionService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StartRunningActionServiceUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(
        runActionId: Long
    ) {
        RunningActionService.start(context, runActionId)
    }
}

class StopRunningActionServiceUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(
        runActionId: Long? = null
    ) {
        RunningActionService.stop(context, runActionId)
    }
}
