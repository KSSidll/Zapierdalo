package com.kssidll.zapierdalo.ui.screen.settings


import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsRoute(
    navigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    SettingsScreen(
        onEvent = { event ->
            when (event) {
                is SettingsEvent.NavigateBack -> {
                    navigateBack()
                }

                is SettingsEvent.SetLocale -> {
                    viewModel.handleEvent(event)
                }
            }
        },
    )
}
