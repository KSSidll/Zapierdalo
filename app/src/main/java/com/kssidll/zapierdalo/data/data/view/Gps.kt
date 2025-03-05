package com.kssidll.zapierdalo.data.data.view

import androidx.room.DatabaseView
import co.anbora.labs.spatia.geometry.Point

@DatabaseView(
    """
        SELECT
            ge.runActionId,
            ge.location
        FROM GpsEntity ge
    """,
    viewName = "Gps"
)
data class Gps(
    val runActionId: Long,
    val location: Point
)
