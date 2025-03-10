package com.kssidll.zapierdalo.data.data.view

import androidx.room.DatabaseView
import androidx.room.SkipQueryVerification
import co.anbora.labs.spatia.geometry.LineString

@SkipQueryVerification
@DatabaseView(
    """
        -- build a simplified linestring from all gps per run action
        SELECT 
            runActionId, 
            ST_Simplify(
                MakeLine(location),
                0.000009 -- ~1 meter tolerance
            ) AS path
        FROM GpsEntity
        GROUP BY runActionId
        ORDER BY id ASC
    """,
    viewName = "GpsPath"
)
data class GpsPath(
    val runActionId: Long,
    val path: LineString
)
