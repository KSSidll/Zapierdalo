package com.kssidll.zapierdalo.ui.screen.dashboard


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kssidll.zapierdalo.domain.data.Data
import com.kssidll.zapierdalo.domain.data.Gps
import com.kssidll.zapierdalo.domain.data.toEntity
import com.kssidll.zapierdalo.domain.usecase.StartRunningActionServiceUseCase
import com.kssidll.zapierdalo.domain.usecase.gps.GetGpsByRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetLatestRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.steps.GetStepsByRunActionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data object DashboardUiStateDefaults {
    val currentDestination: DashboardDestinations = DashboardDestinations.DEFAULT
}

data object DashboardUiStateKeys {
    const val CURRENT_DESTINATION_INDEX = "current_destination"
}

data class DashboardUiState(
    val gpsList: Data<List<Gps>> = Data.Loading(),
    val steps: Data<Long> = Data.Loading(),

    val currentDestination: DashboardDestinations = DashboardUiStateDefaults.currentDestination
)

sealed class DashboardEvent {
    data object NavigateSettings: DashboardEvent()
    data object StartRunningAction: DashboardEvent()
    data class ChangeScreenDestination(val newDestination: DashboardDestinations): DashboardEvent()
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val startRunningActionServiceUseCase: StartRunningActionServiceUseCase,
    private val getLatestRunActionUseCase: GetLatestRunActionUseCase,
    private val getGpsByRunActionUseCase: GetGpsByRunActionUseCase,
    private val getStepsByRunActionUseCaseModule: GetStepsByRunActionUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(
        DashboardUiState(
            currentDestination = DashboardDestinations.get(savedStateHandle[DashboardUiStateKeys.CURRENT_DESTINATION_INDEX])
        )
    )
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getLatestRunActionUseCase().flatMapMerge { runActionData ->
                runActionData()?.toEntity()?.id?.let { runActionId ->
                    getGpsByRunActionUseCase(runActionId)
                } ?: flowOf()
            }
                .collect { gpsList ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            gpsList = gpsList
                        )
                    }
                }
        }

        viewModelScope.launch {
            getLatestRunActionUseCase().flatMapMerge { runActionData ->
                runActionData()?.toEntity()?.id?.let { runActionId ->
                    getStepsByRunActionUseCaseModule(runActionId)
                } ?: flowOf()
            }
                .collect { stepsList ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            steps = Data.Loaded(stepsList()?.lastOrNull()?.steps ?: 0L)
                        )
                    }
                }
        }
    }

    private fun changeScreenDestination(newDestination: DashboardDestinations) {
        _uiState.update { currentState ->
            savedStateHandle[DashboardUiStateKeys.CURRENT_DESTINATION_INDEX] = newDestination.ordinal
            currentState.copy(
                currentDestination = newDestination
            )
        }
    }

    fun handleEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.NavigateSettings -> {}

            is DashboardEvent.StartRunningAction -> {
                startRunningActionServiceUseCase()
            }

            is DashboardEvent.ChangeScreenDestination -> {
                changeScreenDestination(event.newDestination)
            }
        }
    }
}
