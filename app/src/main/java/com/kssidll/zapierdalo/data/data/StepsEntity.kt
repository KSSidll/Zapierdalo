package com.kssidll.zapierdalo.data.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
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
    ]
)
data class StepsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(index = true) val runActionId: Long,
    @ColumnInfo val startTimestamp: Long,
    @ColumnInfo val endTimestamp: Long?,
    @ColumnInfo val steps: Long,
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
