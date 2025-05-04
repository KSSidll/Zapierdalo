package com.kssidll.zapierdalo.data.data.view

import androidx.room.DatabaseView
import androidx.room.SkipQueryVerification

@SkipQueryVerification
@DatabaseView(
    """
        SELECT
            rae.id,
            sa.totalSteps,
            gl.totalLength,
            rae.startTimestamp,
            rae.endTimestamp
        FROM RunActionEntity rae
        LEFT JOIN StepsAmountShadow sa ON sa.runActionId = rae.id
        LEFT JOIN GpsLengthShadow gl on gl.runActionId = rae.id
    """,
    viewName = "RunActionSummary"
)
data class RunActionSummary(
    val id: Long,
    val totalSteps: Long,
    val totalLength: Double,
    val startTimestamp: Long,
    val endTimestamp: Long?
)
