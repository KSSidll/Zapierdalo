package com.kssidll.zapierdalo.ui.screen.dashboard


import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.kssidll.zapierdalo.DAY_IN_MILIS
import com.kssidll.zapierdalo.domain.data.RunAction
import com.kssidll.zapierdalo.domain.usecase.StartRunningActionServiceUseCase
import com.kssidll.zapierdalo.domain.usecase.StopRunningActionServiceUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetAllPagedRunActionUseCase
import com.kssidll.zapierdalo.service.RunningActionService
import com.kssidll.zapierdalo.service.ServiceState
import com.kssidll.zapierdalo.service.getServiceState
import com.kssidll.zapierdalo.service.getSharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

internal data object DashboardUiStateKeys {
    const val CURRENT_DESTINATION_INDEX = "current_destination"
}

data class DashboardUiState(
    val runActionData: Flow<PagingData<RunActionElement>> = flowOf(),

    val currentDestination: DashboardDestinations = DashboardDestinations.DEFAULT,
    val runsScreenListState: LazyListState = LazyListState(),
    val isInRunningAction: Boolean = false,
)

sealed class DashboardEvent {
    data object NavigateSettings: DashboardEvent()
    data class NavigateRunActionDetails(val runAction: RunAction): DashboardEvent()
    data object StartRunningAction: DashboardEvent()
    data object StopRunningAction: DashboardEvent()
    data class ChangeScreenDestination(val newDestination: DashboardDestinations): DashboardEvent()
}

sealed class RunActionElement {
    data class Separator(val date: Long): RunActionElement()
    data class Element(val data: RunAction): RunActionElement()
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val startRunningActionServiceUseCase: StartRunningActionServiceUseCase,
    private val stopRunningActionServiceUseCase: StopRunningActionServiceUseCase,
    private val getAllPagedRunActionUseCase: GetAllPagedRunActionUseCase,
    private val savedStateHandle: SavedStateHandle,
): ViewModel(), SharedPreferences.OnSharedPreferenceChangeListener {
    private val _uiState = MutableStateFlow(
        DashboardUiState(
            currentDestination = DashboardDestinations.get(savedStateHandle[DashboardUiStateKeys.CURRENT_DESTINATION_INDEX]),
            isInRunningAction = context.getServiceState(RunningActionService.SERVICE_NAME) == ServiceState.STARTED
        )
    )
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        context.getSharedPreferences(RunningActionService.SERVICE_NAME)
            .registerOnSharedPreferenceChangeListener(this)

        _uiState.update { currentState ->
            currentState.copy(
                runActionData = getAllPagedRunActionUseCase()
                    .map { flowPagingData ->
                        flowPagingData.map { runAction ->
                            RunActionElement.Element(runAction)
                        }.insertSeparators { before: RunActionElement?, after: RunActionElement? ->
                            if (before == null && after is RunActionElement.Element) {
                                return@insertSeparators RunActionElement.Separator(after.data.startTimestamp)
                            }

                            if (before is RunActionElement.Element && after is RunActionElement.Element) {
                                val beforeDayDate = before.data.startTimestamp / DAY_IN_MILIS
                                val afterDayDate = after.data.startTimestamp / DAY_IN_MILIS

                                if (beforeDayDate != afterDayDate) {
                                    return@insertSeparators RunActionElement.Separator(after.data.startTimestamp)
                                }
                            }

                            null
                        }
                    }
                    .cachedIn(viewModelScope)
            )
        }
    }

    private fun changeScreenDestination(newDestination: DashboardDestinations) {
        _uiState.update { currentState ->
            savedStateHandle[DashboardUiStateKeys.CURRENT_DESTINATION_INDEX] =
                newDestination.ordinal
            currentState.copy(
                currentDestination = newDestination
            )
        }
    }

    fun handleEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.NavigateSettings -> {}

            is DashboardEvent.NavigateRunActionDetails -> {}

            is DashboardEvent.StartRunningAction -> {
                startRunningActionServiceUseCase()
            }

            is DashboardEvent.StopRunningAction -> {
                stopRunningActionServiceUseCase()
            }

            is DashboardEvent.ChangeScreenDestination -> {
                changeScreenDestination(event.newDestination)
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (sharedPreferences == context.getSharedPreferences(RunningActionService.SERVICE_NAME)) {
            _uiState.update { currentState ->
                currentState.copy(
                    isInRunningAction = context.getServiceState(RunningActionService.SERVICE_NAME) == ServiceState.STARTED
                )
            }
        }
    }

    override fun onCleared() {
        context.getSharedPreferences(RunningActionService.SERVICE_NAME)
            .unregisterOnSharedPreferenceChangeListener(this)

        super.onCleared()
    }
}
