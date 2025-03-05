package com.kssidll.zapierdalo.domain.data

import co.anbora.labs.spatia.geometry.Point

data class RunActionDetails(
    val id: Long,
    val totalDistance: Double,
    val totalSteps: Long,
    val startTimestamp: Long,
    val endTimestamp: Long?,
    val gpsPoints: List<Point>
)
