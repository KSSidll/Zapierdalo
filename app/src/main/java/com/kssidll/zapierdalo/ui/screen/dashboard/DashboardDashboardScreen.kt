package com.kssidll.zapierdalo.ui.screen.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.kssidll.zapierdalo.ExpandedPreviews
import com.kssidll.zapierdalo.ui.theme.ZapierdaloTheme

@Composable
fun DashboardDashboardScreen(
    uiState: DashboardUiState,
    onEvent: (event: DashboardEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
    ) { innerPaddingValues ->
        Column(
            modifier = Modifier
                .padding(innerPaddingValues)
                .consumeWindowInsets(innerPaddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Button(
                onClick = {
                    onEvent(DashboardEvent.NavigateSettings)
                }
            ) {
                Text("Settings")
            }
        }
    }
}

@PreviewLightDark
@ExpandedPreviews
@Composable
private fun DashboardScreenDashboardPreview() {
    ZapierdaloTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            DashboardScreen(
                uiState = DashboardUiState(
                    currentDestination = DashboardDestinations.DASHBOARD
                ),
                onEvent = {}
            )
        }
    }
}
