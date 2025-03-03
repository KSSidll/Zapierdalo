package com.kssidll.zapierdalo.domain.data

import co.anbora.labs.spatia.geometry.Point
import org.osmdroid.util.GeoPoint

fun Point.toGeoPoint(): GeoPoint {
    return GeoPoint(x, y)
}

fun List<Point>.toGeoPointList(): List<GeoPoint> {
    return map { it.toGeoPoint() }
}

fun List<Point>.lastGeoPoint(): GeoPoint? {
    return lastOrNull()?.toGeoPoint()
}
