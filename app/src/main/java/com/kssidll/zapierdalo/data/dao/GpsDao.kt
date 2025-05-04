package com.kssidll.zapierdalo.data.dao

import android.R.attr.data
import android.icu.text.MessagePattern.ArgType.SELECT
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.kssidll.zapierdalo.data.data.GpsEntity
import com.kssidll.zapierdalo.data.data.shadow.GpsLengthShadow
import com.kssidll.zapierdalo.helper.toGeoPoint
import kotlinx.coroutines.flow.Flow
import org.osmdroid.util.GeoPoint

@Dao
interface GpsDao {
    // Shadow
    // TODO optimise by adding per RunAction separation

    @Query(GpsLengthShadow.DELETE_QUERY)
    suspend fun mDeleteShadow()

    @Insert
    suspend fun mInsertShadow(shadow: List<GpsLengthShadow>)

    @Transaction
    suspend fun mInsertShadow() {
        val data = mutableListOf<GpsLengthShadow>()

        mAll().filter { it.accuracy < 9f }.groupBy { it.runActionId }.forEach {
            data.add(
                GpsLengthShadow(
                    runActionId = it.key,
                    geoPointList = it.value.toGeoPoint()
                )
            )
        }

        mInsertShadow(data)
    }

    @Transaction
    suspend fun refreshShadow() {
        mDeleteShadow()
        mInsertShadow()
    }

    // Create

    @Transaction
    suspend fun insert(entity: GpsEntity): Long {
        val entityId = mInsert(entity)
        refreshShadow()

        return entityId
    }

    @Insert
    suspend fun mInsert(entity: GpsEntity): Long

    // Delete

    // Read

    @Query("SELECT * FROM GpsEntity")
    suspend fun mAll(): List<GpsEntity>

    @Query("SELECT * FROM GpsEntity WHERE runActionId = :runActionId")
    fun forRunAction(runActionId: Long): Flow<List<GpsEntity>>

}