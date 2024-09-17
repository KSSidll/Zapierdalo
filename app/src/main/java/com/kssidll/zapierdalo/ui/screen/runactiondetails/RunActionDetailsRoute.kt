package com.kssidll.zapierdalo.ui.screen.runactiondetails


import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun RunActionDetailsRoute(
    id: Long,
    viewModel: RunActionDetailsViewModel = hiltViewModel()
) {

    viewModel.init(id)

    RunActionDetailsScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle(minActiveState = Lifecycle.State.RESUMED).value,
    )
}
