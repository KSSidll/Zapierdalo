package com.kssidll.zapierdalo.ui.screen.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.kssidll.zapierdalo.ExpandedPreviews
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

@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onEvent: (event: DashboardEvent) -> Unit,
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

    Scaffold(
        contentWindowInsets = WindowInsets.none,
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        onEvent(DashboardEvent.StartRunningAction)
                    }
                ) {
                    Text("test")
                }

                Spacer(modifier = Modifier.height(24.dp))

                AnimatedVisibility(
                    visible = uiState.steps.isLoaded(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text("Steps: ${uiState.steps()}")
                }

                Spacer(modifier = Modifier.height(24.dp))

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
                                    mapView.isTilesScaledToDpi = true
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
