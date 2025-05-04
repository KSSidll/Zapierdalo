package com.kssidll.zapierdalo.data.data.view

import androidx.room.DatabaseView

@DatabaseView(
    """
        SELECT
            id,
            totalSteps,
            totalLength,
            startTimestamp,
            endTimestamp
        FROM RunActionSummary
    """,
    viewName = "RunAction"
)
data class RunAction(
    val id: Long,
    val totalSteps: Long,
    val totalLength: Double,
    val startTimestamp: Long,
    val endTimestamp: Long?
)