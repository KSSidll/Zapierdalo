package com.kssidll.zapierdalo.data.data.view

import androidx.room.DatabaseView
import androidx.room.SkipQueryVerification
import co.anbora.labs.spatia.geometry.LineString

@SkipQueryVerification
@DatabaseView(
    """
        SELECT
            rae.id,
            gp.path,
            sa.totalSteps,
            rae.startTimestamp,
            rae.endTimestamp
        FROM RunActionEntity rae
        LEFT JOIN GpsPath gp ON gp.runActionId = rae.id
        LEFT JOIN StepsAmount sa ON sa.runActionId = rae.id
    """,
    viewName = "RunAction"
)
data class RunAction(
    val id: Long,
    val path: LineString?,
    val totalSteps: Long,
    val startTimestamp: Long,
    val endTimestamp: Long?
)
