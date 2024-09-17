package com.kssidll.zapierdalo.ui.screen.runactiondetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun RunActionDetailsScreen(
    uiState: RunActionDetailsUiState,
    modifier: Modifier = Modifier,
) {
    /*    var mapView: MapView? by remember { mutableStateOf(null) }
        var mapViewInitialZoomSet: Boolean by remember { mutableStateOf(false) }

        var mapViewPolyline: Polyline? by remember { mutableStateOf(null) }
        val mapViewPolylineColor = MaterialTheme.colorScheme.tertiary

        LaunchedEffect(mapView, uiState.runActionData) {
            if (uiState.runActionData.isLoaded()) {
                mapView?.let { mapView ->
                    if (!mapViewInitialZoomSet) {
                        mapView.controller.setZoom(18.0)
                        mapViewInitialZoomSet = true
                    }

                    mapView.controller.setCenter(uiState.runActionData()?.entity?.lastGeoPoint().orPointZero())

                    if (mapViewPolyline == null || !mapView.overlayManager.contains(mapViewPolyline)) {
                        mapViewPolyline = Polyline().apply {
                            outlinePaint.color = mapViewPolylineColor.toArgb()
                            setPoints(uiState.runActionList()?.asGeoPointList().orEmpty())
                        }
                        mapView.overlayManager.add(mapViewPolyline)
                    } else {
                        mapViewPolyline?.setPoints(uiState.runActionList()?.asGeoPointList().orEmpty())
                    }

                    mapView.invalidate()
                }
            }
        }

        Surface {
            AnimatedVisibility(
                visible = uiState.runActionList.isLoaded(),
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
        }*/
}
