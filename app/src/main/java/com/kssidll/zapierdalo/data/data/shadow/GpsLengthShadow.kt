package com.kssidll.zapierdalo.data.data.shadow

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.kssidll.zapierdalo.helper.totalDistance
import org.osmdroid.util.GeoPoint

@Entity(
    tableName = "GpsLengthShadow"
)
data class GpsLengthShadow(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val runActionId: Long,
    val totalLength: Double
) {
    companion object {
        @Ignore
        const val DELETE_QUERY = """
            DELETE FROM GpsLengthShadow
        """
    }

    @Ignore
    constructor(
        runActionId: Long,
        geoPointList: List<GeoPoint>
    ): this(
        id = 0,
        runActionId = runActionId,
        totalLength = geoPointList.totalDistance()
    )
}

data class GpsLength(
    val runActionId: Long,
    val totalLength: Double
) {
    companion object {
        fun fromShadow(shadow: GpsLengthShadow): GpsLength {
            return GpsLength(
                runActionId = shadow.runActionId,
                totalLength = shadow.totalLength
            )
        }

        fun fromShadow(shadow: List<GpsLengthShadow>): List<GpsLength> {
            return shadow.map {
                fromShadow(it)
            }
        }
    }
}
