package com.kssidll.zapierdalo.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kssidll.zapierdalo.data.data.GpsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GpsDao {
    // Create

    @Insert
    suspend fun insert(entity: GpsEntity): Long

    // Delete

    @Delete
    suspend fun delete(entity: GpsEntity)

    // Read

    @Query("SELECT GpsEntity.* FROM GpsEntity WHERE GpsEntity.runActionId = :runActionId")
    fun byRunActionId(runActionId: Long): Flow<List<GpsEntity>>

    @Query("SELECT GpsEntity.* FROM GpsEntity")
    fun all(): Flow<List<GpsEntity>>
}