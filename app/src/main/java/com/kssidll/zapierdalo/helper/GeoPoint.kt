package com.kssidll.zapierdalo.helper

import androidx.compose.ui.util.fastForEachIndexed
import com.kssidll.zapierdalo.data.data.GpsEntity
import org.osmdroid.util.GeoPoint

fun GpsEntity.toGeoPoint(): GeoPoint {
    return GeoPoint(latitude, longitude, altitude)
}

fun List<GpsEntity>.toGeoPoint(): List<GeoPoint> {
    return map { it.toGeoPoint() }
}

fun GeoPoint?.orPointZero(): GeoPoint {
    return this ?: GeoPoint(0.0, 0.0, 0.0)
}

fun List<GeoPoint>.totalDistance(): Double {
    var sum = 0.0

    fastForEachIndexed { itr, geoPoint ->
        if (itr == 0) return@fastForEachIndexed

        val dist = geoPoint.distanceToAsDouble(get(itr - 1))

        sum += dist
    }

    return sum
}

fun List<GeoPoint>.kalmanSmoothing(): List<GeoPoint> {
    val kalmanFilterLat = KalmanFilter()
    val kalmanFilterLon = KalmanFilter()
    val kalmanFilterAlt = KalmanFilter()

    return map { location ->
        val smoothedLat = kalmanFilterLat.update(location.latitude)
        val smoothedLon = kalmanFilterLon.update(location.longitude)
        val smoothedAlt = kalmanFilterAlt.update(location.altitude)

        GeoPoint(smoothedLat, smoothedLon, smoothedAlt)
    }
}

class KalmanFilter(private val q: Double = 0.000001, private val r: Double = 0.0001) {
    private var x: Double = 0.0  // estimated value
    private var p: Double = 1.0  // estimation error covariance

    fun update(measurement: Double): Double {
        // prediction update
        p += q

        // measurement update
        val k = p / (p + r)
        x += k * (measurement - x)
        p *= (1 - k)

        return x
    }
}