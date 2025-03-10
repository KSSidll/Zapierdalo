package com.kssidll.zapierdalo.data.data.view

import androidx.room.DatabaseView
import androidx.room.SkipQueryVerification

@SkipQueryVerification
@DatabaseView(
    """
        SELECT
            id,
            ST_Length(path, 1) AS totalDistance,
            totalSteps,
            startTimestamp,
            endTimestamp
        FROM RunAction
    """,
    viewName = "RunActionSummary"
)
data class RunActionSummary(
    val id: Long,
    val totalDistance: Double,
    val totalSteps: Long,
    val startTimestamp: Long,
    val endTimestamp: Long?
)
