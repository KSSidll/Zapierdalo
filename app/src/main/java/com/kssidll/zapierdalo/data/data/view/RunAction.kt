package com.kssidll.zapierdalo.data.data.view

import androidx.room.DatabaseView

@DatabaseView(
    """
        SELECT
            rae.id,
            gd.totalDistance,
            sa.totalSteps,
            rae.startTimestamp,
            rae.endTimestamp
        FROM RunActionEntity rae
        LEFT JOIN GpsDistance gd ON gd.runActionId = rae.id
        LEFT JOIN StepsAmount sa ON sa.runActionId = rae.id
    """,
    viewName = "RunAction"
)
data class RunAction(
    val id: Long,
    val totalDistance: Double,
    val totalSteps: Long,
    val startTimestamp: Long,
    val endTimestamp: Long?
)
