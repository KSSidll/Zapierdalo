package com.kssidll.zapierdalo.data.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import co.anbora.labs.spatia.geometry.Point
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
        Index(value = ["runActionId"]),
        Index(value = ["runActionId", "id"]),
    ],
    tableName = "GpsEntity"
)
data class GpsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val runActionId: Long,
    val location: Point,
    val accuracy: Float,
    val speed: Float, // in kmh
    val timestamp: Long,
) {
    @Ignore
    constructor(
        runActionId: Long,
        location: Point,
        accuracy: Float,
        speed: Float, // in kmh
        timestamp: Long = Calendar.getInstance().timeInMillis,
    ): this(
        id = 0,
        runActionId = runActionId,
        location = location,
        accuracy = accuracy,
        speed = speed,
        timestamp = timestamp,
    )
}
