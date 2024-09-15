package com.kssidll.zapierdalo.ui.screen.dashboard


import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.kssidll.zapierdalo.AppState
import com.kssidll.zapierdalo.service.RunningActionService

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DashboardRoute(
    appState: AppState,
    navigateSettings: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val requestLocationPermissionLauncher = rememberMultiplePermissionsState(
        permissions = RunningActionService.Permissions.ALL.asList(),
        onPermissionsResult = { permissionResultMap ->
            if (permissionResultMap.all { it.value }) {
                viewModel.handleEvent(DashboardEvent.StartRunningAction)
            }
        }
    )

    DashboardScreen(
        uiState = viewModel.uiState.collectAsStateWithLifecycle(minActiveState = Lifecycle.State.RESUMED).value,
        onEvent = { event ->
            when (event) {
                is DashboardEvent.NavigateSettings -> {
                    navigateSettings()
                }

                is DashboardEvent.StartRunningAction -> {
                    requestLocationPermissionLauncher.launchMultiplePermissionRequest()
                }
            }
        },
    )
}
