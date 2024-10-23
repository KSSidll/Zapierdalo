package com.kssidll.zapierdalo.ui.screen.runactiondetails


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kssidll.zapierdalo.NavigationDestinations
import com.kssidll.zapierdalo.domain.data.Data
import com.kssidll.zapierdalo.domain.data.RunActionDetails
import com.kssidll.zapierdalo.domain.usecase.runaction.GetRunActionDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RunActionDetailsUiState(
    val runActionDetails: Data<out RunActionDetails?> = Data.Loading()
)

sealed class RunActionDetailsEvent {
    data object NavigateBack: RunActionDetailsEvent()
}

@HiltViewModel
class RunActionDetailsViewModel @Inject constructor(
    private val getRunActionDetailsUseCase: GetRunActionDetailsUseCase,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _uiState = MutableStateFlow(
        RunActionDetailsUiState()
    )
    val uiState: StateFlow<RunActionDetailsUiState> = _uiState.asStateFlow()

    init {
        val runActionEntityId =
            savedStateHandle.toRoute<NavigationDestinations.RunActionDetails>().runActionEntityId

        viewModelScope.launch {
            getRunActionDetailsUseCase(runActionEntityId).collect {
                _uiState.update { currentState ->
                    currentState.copy(
                        runActionDetails = it
                    )
                }
            }
        }
    }

    fun handleEvent(event: RunActionDetailsEvent) {
        when (event) {
            RunActionDetailsEvent.NavigateBack -> {}
        }
    }
}
