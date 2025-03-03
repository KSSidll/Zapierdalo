package com.kssidll.zapierdalo.data.data

import androidx.compose.ui.util.fastSumBy
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = RunActionEntity::class,
            parentColumns = ["id"],
            childColumns = ["runActionId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["runActionId"])
    ],
    tableName = "StepsEntity"
)
data class StepsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val runActionId: Long,
    val startTimestamp: Long,
    val endTimestamp: Long?,
    val steps: Long,
) {
    @Ignore
    constructor(
        runActionId: Long,
        startTimestamp: Long = Calendar.getInstance().timeInMillis,
        endTimestamp: Long? = null,
        steps: Long = 0
    ): this(
        id = 0,
        runActionId = runActionId,
        startTimestamp = startTimestamp,
        endTimestamp = endTimestamp,
        steps = steps
    )
}

fun List<StepsEntity>.totalSteps(): Long {
    return fastSumBy { it.steps.toInt() }.toLong()
}
