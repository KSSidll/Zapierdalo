package com.kssidll.zapierdalo.ui.screen.runactiondetails


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kssidll.zapierdalo.domain.data.Data
import com.kssidll.zapierdalo.domain.data.RunAction
import com.kssidll.zapierdalo.domain.usecase.runaction.GetRunActionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RunActionDetailsUiState(
    val runActionData: Data<out RunAction?> = Data.Loading()
)

@HiltViewModel
class RunActionDetailsViewModel @Inject constructor(
    private val getRunActionUseCase: GetRunActionUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(
        RunActionDetailsUiState()
    )
    val uiState: StateFlow<RunActionDetailsUiState> = _uiState.asStateFlow()

    private var collectionJob: Job? = null

    fun init(id: Long) {
        collectionJob?.cancel()
        collectionJob = viewModelScope.launch {
            getRunActionUseCase(id).collect {
                _uiState.update { currentState ->
                    currentState.copy(
                        runActionData = it
                    )
                }
            }
        }
    }
}
