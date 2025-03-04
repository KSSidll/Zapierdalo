package com.kssidll.zapierdalo.data.data.view

import androidx.room.DatabaseView
import androidx.room.Ignore
import org.osmdroid.util.GeoPoint

@DatabaseView(
    """
        SELECT
            id,
            runActionId,
            latitude,
            longitude,
            altitude
        FROM GpsEntity
    """,
    viewName = "Gps"
)
data class Gps(
    val id: Long,
    val runActionId: Long,

    private val latitude: Double,
    private val longitude: Double,
    private val altitude: Double,

    @Ignore
    val location: GeoPoint
) {
    constructor(
        id: Long,
        runActionId: Long,
        latitude: Double,
        longitude: Double,
        altitude: Double,
    ): this(
        id = id,
        runActionId = runActionId,
        latitude = latitude,
        longitude = longitude,
        altitude = altitude,
        location = GeoPoint(latitude, longitude, altitude)
    )
}
