package com.kssidll.zapierdalo.data.data.view;

import androidx.room.DatabaseView

@DatabaseView(
    """
        SELECT
            runActionId,
            SUM(steps) as totalSteps
        FROM StepsEntity
        GROUP BY runActionId
    """,
    viewName = "StepsAmount"
)
data class StepsAmount(
    val runActionId: Long,
    val totalSteps: Long
)
