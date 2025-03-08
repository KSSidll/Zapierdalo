package com.kssidll.zapierdalo.domain.data

import co.anbora.labs.spatia.geometry.LineString
import org.osmdroid.util.GeoPoint

fun LineString.toGeoPointList(): List<GeoPoint> {
    return points.toGeoPointList()
}