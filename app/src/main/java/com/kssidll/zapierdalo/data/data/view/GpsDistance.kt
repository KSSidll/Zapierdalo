package com.kssidll.zapierdalo.data.data.view

import androidx.room.DatabaseView
import androidx.room.SkipQueryVerification

@SkipQueryVerification
@DatabaseView(
    """
        SELECT 
            g1.runActionId, 
            SUM(
                ST_DISTANCE(g1.location, g2.location)
            ) AS totalDistance
        FROM 
            GpsEntity g1
        JOIN 
            GpsEntity g2 ON g1.runActionId = g2.runActionId 
                AND g1.id = (
                    SELECT MAX(g3.id) 
                    FROM GpsEntity g3 
                    WHERE g3.id < g2.id AND g3.runActionId = g2.runActionId
                )
        GROUP BY 
            g1.runActionId
    """,
    viewName = "GpsDistance"
)
data class GpsDistance(
    val runActionId: Long,
    val totalDistance: Double
)
