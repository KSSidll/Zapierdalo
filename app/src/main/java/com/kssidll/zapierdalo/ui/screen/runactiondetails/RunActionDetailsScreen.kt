package com.kssidll.zapierdalo.ui.screen.runactiondetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kssidll.zapierdalo.domain.data.asGeoPointList
import com.kssidll.zapierdalo.domain.data.isLoaded
import com.kssidll.zapierdalo.domain.data.lastGeoPoint
import com.kssidll.zapierdalo.helper.none
import com.kssidll.zapierdalo.helper.orPointZero
import com.kssidll.zapierdalo.ui.component.SecondaryAppBar
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunActionDetailsScreen(
    uiState: RunActionDetailsUiState,
    onEvent: (event: RunActionDetailsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var mapView: MapView? by remember { mutableStateOf(null) }
    var mapViewInitialZoomSet: Boolean by remember { mutableStateOf(false) }

    var mapViewPolyline: Polyline? by remember { mutableStateOf(null) }
    val mapViewPolylineColor = MaterialTheme.colorScheme.tertiary

    var sfpo: SimpleFastPointOverlay? by remember { mutableStateOf(null) }

    val gpsPoints = uiState.runActionDetails()?.gpsPoints?.collectAsStateWithLifecycle(emptyList(), minActiveState = Lifecycle.State.RESUMED)?.value.orEmpty()

    LaunchedEffect(mapView, uiState.runActionDetails, gpsPoints) {
        if (uiState.runActionDetails.isLoaded()) {
            mapView?.let { mapView ->
                if (!mapViewInitialZoomSet) {
                    mapView.controller.setZoom(18.0)
                    mapViewInitialZoomSet = true
                }

                mapView.controller.setCenter(gpsPoints.lastGeoPoint().orPointZero())

                if (mapViewPolyline == null || !mapView.overlayManager.contains(mapViewPolyline)) {
                    mapViewPolyline = Polyline().apply {
                        outlinePaint.color = mapViewPolylineColor.toArgb()
                        setPoints(gpsPoints.asGeoPointList())
                    }
                    mapView.overlayManager.add(mapViewPolyline)
                } else {
                    mapViewPolyline?.setPoints(gpsPoints.asGeoPointList())
                }

                if (sfpo != null) {
                    mapView.overlayManager.remove(sfpo)
                }

                val pt = SimplePointTheme(gpsPoints.asGeoPointList())
                val opt = SimpleFastPointOverlayOptions.getDefaultStyle()
                    .setAlgorithm(SimpleFastPointOverlayOptions.RenderingAlgorithm.MEDIUM_OPTIMIZATION)
                    .setRadius(5f)
                    .setCellSize(15)
                sfpo = SimpleFastPointOverlay(pt, opt)

                mapView.overlays.add(sfpo)

                mapView.invalidate()
            }
        }
    }

    Scaffold(
        bottomBar = {
            SecondaryAppBar(
                title = {},
                onBack = {
                    onEvent(RunActionDetailsEvent.NavigateBack)
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        ) {
            AnimatedVisibility(
                visible = uiState.runActionDetails.isLoaded(),
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
}
