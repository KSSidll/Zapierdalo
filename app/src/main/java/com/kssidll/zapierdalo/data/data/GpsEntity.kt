package com.kssidll.zapierdalo.data.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(
    indices = [
        Index(value = ["runActionId"]),
        Index(value = ["runActionId", "id"]),
    ],
    tableName = "GpsEntity"
)
data class GpsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val runActionId: Long,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val accuracy: Float,
    val speed: Float, // in kmh
    val timestamp: Long,
) {
    @Ignore
    constructor(
        runActionId: Long,
        latitude: Double,
        longitude: Double,
        altitude: Double,
        accuracy: Float,
        speed: Float, // in kmh
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
