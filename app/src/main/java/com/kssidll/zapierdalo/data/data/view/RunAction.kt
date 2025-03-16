package com.kssidll.zapierdalo.data.data.view

import androidx.room.DatabaseView

@DatabaseView(
    """
        SELECT
            rae.id,
            sa.totalSteps,
            rae.startTimestamp,
            rae.endTimestamp
        FROM RunActionEntity rae
        LEFT JOIN StepsAmountShadow sa ON sa.runActionId = rae.id
    """,
    viewName = "RunAction"
)
data class RunAction(
    val id: Long,
    val totalSteps: Long,
    val startTimestamp: Long,
    val endTimestamp: Long?
)
