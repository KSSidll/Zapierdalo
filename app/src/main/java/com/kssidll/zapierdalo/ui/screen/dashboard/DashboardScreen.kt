package com.kssidll.zapierdalo.ui.screen.dashboard

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.viewinterop.AndroidView
import com.kssidll.zapierdalo.ExpandedPreviews
import com.kssidll.zapierdalo.R
import com.kssidll.zapierdalo.domain.data.asGeoPointList
import com.kssidll.zapierdalo.domain.data.isLoaded
import com.kssidll.zapierdalo.domain.data.lastGeoPoint
import com.kssidll.zapierdalo.domain.data.loadedData
import com.kssidll.zapierdalo.domain.data.orPointZero
import com.kssidll.zapierdalo.helper.none
import com.kssidll.zapierdalo.ui.theme.ZapierdaloTheme
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline

enum class DashboardDestinations(
    @StringRes val label: Int,
    val disabledIcon: ImageVector,
    val enabledIcon: ImageVector,
    @StringRes val contentDescription: Int
) {
    DASHBOARD(R.string.dashboard, Icons.Outlined.Dashboard, Icons.Filled.Dashboard, R.string.dashboard),
    RUNS(R.string.runs, Icons.AutoMirrored.Outlined.DirectionsRun, Icons.AutoMirrored.Filled.DirectionsRun, R.string.runs)

    ;
    companion object {
        val DEFAULT = DASHBOARD
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
        }
    ) {
        Scaffold(
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {

                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = null
                    )
                }
            },
            contentWindowInsets = WindowInsets.none
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

            }
        }
    }
}

@Composable
fun Test(
    uiState: DashboardUiState,
    modifier: Modifier = Modifier,
) {
    var mapView: MapView? by remember { mutableStateOf(null) }
    var mapViewInitialZoomSet: Boolean by remember { mutableStateOf(false) }

    var mapViewPolyline: Polyline? by remember { mutableStateOf(null) }
    val mapViewPolylineColor = MaterialTheme.colorScheme.tertiary

    LaunchedEffect(mapView, uiState.gpsList()) {
        if (uiState.gpsList.loadedData()) {
            mapView?.let { mapView ->
                if (!mapViewInitialZoomSet) {
                    mapView.controller.setZoom(18.0)
                    mapViewInitialZoomSet = true
                }

                mapView.controller.setCenter(uiState.gpsList()?.lastGeoPoint().orPointZero())

                if (mapViewPolyline == null || !mapView.overlayManager.contains(mapViewPolyline)) {
                    mapViewPolyline = Polyline().apply {
                        outlinePaint.color = mapViewPolylineColor.toArgb()
                        setPoints(uiState.gpsList()?.asGeoPointList().orEmpty())
                    }
                    mapView.overlayManager.add(mapViewPolyline)
                } else {
                    mapViewPolyline?.setPoints(uiState.gpsList()?.asGeoPointList().orEmpty())
                }

                mapView.invalidate()
            }
        }
    }

    Surface {
        AnimatedVisibility(
            visible = uiState.gpsList.isLoaded(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    mapView = MapView(context)
                    mapView?.let { mapView ->
                        mapView.setTileSource(TileSourceFactory.MAPNIK)
                        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
                        mapView.controller.setZoom(3.0)
                        mapView.setMultiTouchControls(true)
                    }
                    mapView!!
                }
            )
        }
    }
}

@PreviewLightDark
@ExpandedPreviews
@Composable
private fun DashboardScreenPreview() {
    ZapierdaloTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            DashboardScreen(
                uiState = DashboardUiState(),
                onEvent = {}
            )
        }
    }
}
