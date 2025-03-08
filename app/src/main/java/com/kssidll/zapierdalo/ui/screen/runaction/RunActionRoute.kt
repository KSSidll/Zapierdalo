package com.kssidll.zapierdalo.ui.screen.runaction


import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun RunActionRoute(
    navigateBack: () -> Unit,
    viewModel: RunActionViewModel = hiltViewModel()
) {
    RunActionScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle(minActiveState = Lifecycle.State.RESUMED).value,
        onEvent = { event ->
            when (event) {
                is RunActionEvent.NavigateBack -> navigateBack()

                is RunActionEvent.Delete -> {
                    viewModel.handleEvent(event)
                    navigateBack()
                }
            }
        }
    )
}
