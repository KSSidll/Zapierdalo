package com.kssidll.zapierdalo.data.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.Calendar

@Entity
@Serializable
data class RunActionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo val startTimestamp: Long,
    @ColumnInfo val endTimestamp: Long?,
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