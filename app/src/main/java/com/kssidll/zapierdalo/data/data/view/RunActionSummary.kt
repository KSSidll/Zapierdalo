package com.kssidll.zapierdalo.data.data.view

import androidx.room.DatabaseView
import androidx.room.SkipQueryVerification

@SkipQueryVerification
@DatabaseView(
    """
        SELECT
            id,
            totalSteps,
            startTimestamp,
            endTimestamp
        FROM RunAction
    """,
    viewName = "RunActionSummary"
)
data class RunActionSummary(
    val id: Long,
    val totalSteps: Long,
    val startTimestamp: Long,
    val endTimestamp: Long?
)
