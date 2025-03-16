package com.kssidll.zapierdalo.data.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(
    indices = [
        Index(value = ["runActionId"])
    ],
    tableName = "StepsEntity"
)
data class StepsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val runActionId: Long,
    val steps: Long,
    val timestamp: Long,
) {
    @Ignore
    constructor(
        runActionId: Long,
        steps: Long,
        timestamp: Long = Calendar.getInstance().timeInMillis,
    ): this(
        id = 0,
        runActionId = runActionId,
        steps = steps,
        timestamp = timestamp,
    )
}
