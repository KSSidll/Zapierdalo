package com.kssidll.zapierdalo.domain.data

import org.osmdroid.util.GeoPoint

data class RunActionDetails(
    val id: Long,
    val totalDistance: Double,
    val totalSteps: Long,
    val startTimestamp: Long,
    val endTimestamp: Long?,
    val gpsPoints: List<GeoPoint>
)
