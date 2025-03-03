package com.kssidll.zapierdalo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kssidll.zapierdalo.data.data.StepsEntity

@Dao
interface StepsDao {
    // Create

    @Insert
    suspend fun insert(entity: StepsEntity): Long

    // Update

    @Query("UPDATE StepsEntity SET endTimestamp = :endTimestamp WHERE id = :entityId")
    suspend fun setEndTimestamp(entityId: Long, endTimestamp: Long?)

    @Query("UPDATE StepsEntity SET steps = :count WHERE id = :entityId")
    suspend fun setStepsCount(entityId: Long, count: Long)

    // Delete

    // Read

}