package com.kssidll.zapierdalo.domain.data

import com.kssidll.zapierdalo.data.data.GpsEntity
import com.kssidll.zapierdalo.data.data.geoPoint
import com.kssidll.zapierdalo.helper.totalDistance
import org.osmdroid.util.GeoPoint

data class Gps(
    val entity: GpsEntity,
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val accuracy: Float,
    val speed: Float,
)

fun Gps.toEntity() = this.entity

fun GpsEntity.toDomain() = Gps(
    entity = this,
    timestamp = timestamp,
    latitude = latitude,
    longitude = longitude,
    altitude = altitude,
    accuracy = accuracy,
    speed = speed,
)

fun Gps.geoPoint(): GeoPoint {
    return toEntity().geoPoint()
}

fun List<Gps>.asGeoPointList(): List<GeoPoint> {
    return map { it.geoPoint() }
}

fun List<Gps>.lastGeoPoint(): GeoPoint? {
    return lastOrNull()?.geoPoint()
}

fun List<Gps>.totalDistance(): Double {
    return asGeoPointList().totalDistance()
}