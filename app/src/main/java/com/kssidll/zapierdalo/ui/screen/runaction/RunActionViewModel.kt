package com.kssidll.zapierdalo.ui.screen.runaction


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kssidll.zapierdalo.NavigationDestinations
import com.kssidll.zapierdalo.data.data.view.RunAction
import com.kssidll.zapierdalo.domain.usecase.runaction.DeleteRunActionUseCase
import com.kssidll.zapierdalo.domain.usecase.runaction.GetRunActionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RunActionUiState(
    val runAction: RunAction? = null
)

sealed class RunActionEvent {
    data object NavigateBack: RunActionEvent()
    data object Delete: RunActionEvent()
}

@HiltViewModel
class RunActionViewModel @Inject constructor(
    private val getRunActionUseCase: GetRunActionUseCase,
    private val deleteRunActionEntityUseCase: DeleteRunActionUseCase,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _uiState = MutableStateFlow(
        RunActionUiState()
    )
    val uiState: StateFlow<RunActionUiState> = _uiState.asStateFlow()

    init {
        val runActionEntityId =
            savedStateHandle.toRoute<NavigationDestinations.RunAction>().runActionEntityId

        viewModelScope.launch {
            getRunActionUseCase(runActionEntityId).collect {
                _uiState.update { currentState ->
                    currentState.copy(
                        runAction = it
                    )
                }
            }
        }
    }

    fun handleEvent(event: RunActionEvent) {
        when (event) {
            is RunActionEvent.NavigateBack -> {}

            is RunActionEvent.Delete -> deleteRunAction()
        }
    }

    private fun deleteRunAction() = viewModelScope.launch {
        val localState = uiState.value
        localState.runAction?.let {
            deleteRunActionEntityUseCase(it.id)
        }
    }
}
