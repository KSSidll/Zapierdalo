package com.kssidll.zapierdalo.ui.screen.dashboard


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

data class DashboardUiState(
    val gpsList: Data<List<Gps>> = Data.Loading(),
    val steps: Data<Long> = Data.Loading()
)

sealed class DashboardEvent {
    data object NavigateSettings: DashboardEvent()
    data object StartRunningAction: DashboardEvent()
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val startRunningActionServiceUseCase: StartRunningActionServiceUseCase,
    private val getLatestRunActionUseCase: GetLatestRunActionUseCase,
    private val getGpsByRunActionUseCase: GetGpsByRunActionUseCase,
    private val getStepsByRunActionUseCaseModule: GetStepsByRunActionUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
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

    fun handleEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.NavigateSettings -> {}

            is DashboardEvent.StartRunningAction -> {
                startRunningActionServiceUseCase()
            }
        }
    }
}
