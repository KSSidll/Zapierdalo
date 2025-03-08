package com.kssidll.zapierdalo.ui.screen.dashboard

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.kssidll.zapierdalo.R
import com.kssidll.zapierdalo.helper.none
import com.kssidll.zapierdalo.ui.screen.dashboard.DashboardDestinations.entries

enum class DashboardDestinations(
    @StringRes val label: Int,
    val disabledIcon: ImageVector,
    val enabledIcon: ImageVector,
    @StringRes val contentDescription: Int
) {
    DASHBOARD(
        R.string.dashboard,
        Icons.Outlined.Dashboard,
        Icons.Filled.Dashboard,
        R.string.dashboard
    ),
    RUNS(
        R.string.runs,
        Icons.AutoMirrored.Outlined.DirectionsRun,
        Icons.AutoMirrored.Filled.DirectionsRun,
        R.string.runs
    )

    ;

    companion object {
        val DEFAULT = RUNS
        fun get(index: Int?) = index?.let { entries.getOrElse(it) { DEFAULT } } ?: DEFAULT
    }
}

@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onEvent: (event: DashboardEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            DashboardDestinations.entries.forEach {
                item(
                    icon = {
                        Crossfade(
                            it == uiState.currentDestination,
                            label = "destination change (icon)"
                        ) { selected ->
                            Icon(
                                imageVector = if (selected) it.enabledIcon else it.disabledIcon,
                                contentDescription = stringResource(it.contentDescription)
                            )
                        }
                    },
                    label = {
                        Text(stringResource(it.label))
                    },
                    selected = it == uiState.currentDestination,
                    onClick = {
                        onEvent(DashboardEvent.ChangeScreenDestination(it))
                    }
                )
            }
        },
        modifier = modifier
    ) {
        Scaffold(
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        when (uiState.isInRunningAction) {
                            true -> onEvent(DashboardEvent.StopRunningAction)
                            false -> onEvent(DashboardEvent.StartRunningAction)
                        }
                    }
                ) {
                    Crossfade(
                        targetState = uiState.isInRunningAction,
                        label = ""
                    ) { running ->
                        when (running) {
                            true -> {
                                Icon(
                                    imageVector = Icons.Filled.Stop,
                                    contentDescription = stringResource(R.string.stop)
                                )
                            }

                            false -> {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = stringResource(R.string.start)
                                )
                            }
                        }
                    }
                }
            },
            contentWindowInsets = WindowInsets.none
        ) { paddingValues ->
            Crossfade(
                targetState = uiState.currentDestination,
                label = "dashboard destination change crossfade",
                modifier = Modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
            ) { destinations ->
                when (destinations) {
                    DashboardDestinations.DASHBOARD -> {
                        DashboardDashboardScreen(
                            uiState = uiState,
                            onEvent = onEvent,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    DashboardDestinations.RUNS -> {
                        DashboardRunsScreen(
                            uiState = uiState,
                            onEvent = onEvent,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
