package com.kssidll.zapierdalo.domain.data

import com.kssidll.zapierdalo.data.data.GpsEntity
import org.osmdroid.util.GeoPoint

data class Gps(
    val entity: GpsEntity,
    val runAction: RunAction,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val accuracy: Float,
    val speed: Float,
)

fun Gps.toEntity() = this.entity

fun GpsEntity.toDomain(runAction: RunAction) = Gps(
    entity = this,
    runAction = runAction,
    timestamp = timestamp,
    latitude = latitude,
    longitude = longitude,
    altitude = altitude,
    accuracy = accuracy,
    speed = speed,
)

fun Gps.geoPoint(): GeoPoint {
    return GeoPoint(latitude, longitude, altitude)
}

fun List<Gps>.asGeoPointList(): List<GeoPoint> {
    return map { it.geoPoint() }
}

fun List<Gps>.lastGeoPoint(): GeoPoint? {
    return lastOrNull()?.geoPoint()
}

fun GeoPoint?.orPointZero(): GeoPoint {
    return this ?: GeoPoint(0.0, 0.0, 0.0)
}