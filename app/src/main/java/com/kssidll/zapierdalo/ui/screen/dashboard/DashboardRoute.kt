package com.kssidll.zapierdalo.ui.screen.dashboard


import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kssidll.zapierdalo.domain.data.toEntity
import com.kssidll.zapierdalo.service.rememberRunningActionServicePreparationLauncher

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DashboardRoute(
    navigateSettings: () -> Unit,
    navigateRunActionDetails: (runActionEntityId: Long) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val runningActionServicePreparationLauncher = rememberRunningActionServicePreparationLauncher(
        onSuccess = {
            viewModel.handleEvent(DashboardEvent.StartRunningAction)
        },
        onFailure = {}
    )

    DashboardScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle(minActiveState = Lifecycle.State.RESUMED).value,
        onEvent = { event ->
            when (event) {
                is DashboardEvent.NavigateSettings -> {
                    navigateSettings()
                }

                is DashboardEvent.NavigateRunActionDetails -> {
                    navigateRunActionDetails(event.runAction.toEntity().id)
                }

                is DashboardEvent.StartRunningAction -> {
                    runningActionServicePreparationLauncher.launchMultiplePermissionRequest()
                }

                is DashboardEvent.StopRunningAction -> {
                    viewModel.handleEvent(event)
                }

                is DashboardEvent.ChangeScreenDestination -> {
                    viewModel.handleEvent(event)
                }
            }
        },
    )
}
