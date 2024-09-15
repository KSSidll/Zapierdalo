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
data class GpsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(index = true) val runActionId: Long,
    @ColumnInfo val latitude: Double,
    @ColumnInfo val longitude: Double,
    @ColumnInfo val altitude: Double,
    @ColumnInfo val accuracy: Float,
    @ColumnInfo val speed: Float,
    @ColumnInfo val timestamp: Long,
) {
    @Ignore
    constructor(
        runActionId: Long,
        latitude: Double,
        longitude: Double,
        altitude: Double,
        accuracy: Float,
        speed: Float,
        timestamp: Long = Calendar.getInstance().timeInMillis,
    ): this(
        id = 0,
        runActionId = runActionId,
        latitude = latitude,
        longitude = longitude,
        altitude = altitude,
        accuracy = accuracy,
        speed = speed,
        timestamp = timestamp,
    )
}