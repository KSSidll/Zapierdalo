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
                ST_LineFromText(
                    'LINESTRING(' || GROUP_CONCAT(ST_X(location) || ' ' || ST_Y(location), ',') || ')',
                    ST_SRID(location)
                ),
                5 -- 5 meters tolerance for simplification
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
