package com.kssidll.zapierdalo.data.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(
    tableName = "RunActionEntity",
    indices = [
        Index(value = ["id"])
    ]
)
data class RunActionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val startTimestamp: Long,
    val endTimestamp: Long?,
) {
    constructor(
        startTimestamp: Long = Calendar.getInstance().timeInMillis,
        endTimestamp: Long? = null
    ): this(
        id = 0,
        startTimestamp = startTimestamp,
        endTimestamp = endTimestamp
    )
}