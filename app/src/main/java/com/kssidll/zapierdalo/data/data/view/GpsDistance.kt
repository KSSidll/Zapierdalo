package com.kssidll.zapierdalo.data.data.view

import androidx.room.DatabaseView
import androidx.room.SkipQueryVerification

@SkipQueryVerification
@DatabaseView(
    """
        -- build a simplified linestring from all gps per run action and calculate length in meters
        SELECT 
            runActionId, 
            ST_Length(
                ST_Simplify(
                    ST_LineFromText(
                        'LINESTRING(' || GROUP_CONCAT(ST_X(location) || ' ' || ST_Y(location), ',') || ')',
                        ST_SRID(location)
                    ),
                    5 -- 5 meters tolerance for simplification
                )
            ) AS totalDistance
        FROM GpsEntity
        GROUP BY runActionId
        ORDER BY id ASC
    """,
    viewName = "GpsDistance"
)
data class GpsDistance(
    val runActionId: Long,
    val totalDistance: Double
)
